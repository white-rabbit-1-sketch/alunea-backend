package me.silvermail.backend.controller;

import jakarta.validation.Valid;
import me.silvermail.backend.dto.controller.request.user.*;
import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.dto.controller.response.user.UserEmailAvailabilityResponseDto;
import me.silvermail.backend.dto.controller.response.user.UserResponseDto;
import me.silvermail.backend.exception.http.TooManyRequestsHttpException;
import me.silvermail.backend.exception.http.UnauthorizedHttpException;
import me.silvermail.backend.model.email.internal.UserEmailVerificationEmail;
import me.silvermail.backend.model.email.internal.UserPasswordChangedEmail;
import me.silvermail.backend.model.email.internal.UserPasswordResetEmail;
import me.silvermail.backend.entity.Mailbox;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.domain.*;
import me.silvermail.backend.entity.token.UserEmailVerificationToken;
import me.silvermail.backend.entity.token.UserPasswordResetToken;
import me.silvermail.backend.exception.http.AccessDeniedHttpException;
import me.silvermail.backend.service.InternalEmailService;
import me.silvermail.backend.service.MailboxService;
import me.silvermail.backend.service.ThrottlerService;
import me.silvermail.backend.service.UserService;
import me.silvermail.backend.service.token.UserEmailVerificationTokenService;
import me.silvermail.backend.service.token.UserPasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    protected UserService userService;
    @Autowired
    protected MailboxService mailboxService;
    @Autowired
    protected InternalEmailService emailService;
    @Autowired
    protected UserPasswordResetTokenService userPasswordResetTokenService;
    @Autowired
    protected UserEmailVerificationTokenService userEmailVerificationTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ThrottlerService throttlerService;

    @GetMapping(path="/email/{email}")
    public UserEmailAvailabilityResponseDto isUserEmailAvailable(
            @PathVariable String email
    ) {
        User user = this.userService.getUserByEmail(email);

        UserEmailAvailabilityResponseDto userEmailAvailableResponseDto = new UserEmailAvailabilityResponseDto();
        userEmailAvailableResponseDto.setAvailable(user == null);

        return userEmailAvailableResponseDto;
    }

    @GetMapping(path="/{userId}")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public UserResponseDto getUserById(
            @PathVariable("userId") User user
    ) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUser(user);

        return userResponseDto;
    }

    @PostMapping("")
    public UserResponseDto createUser(
            @RequestBody @Valid CreateUserRequestDto createUserRequestDto
    ) throws Exception {
        User user = userService.createUserWithMailbox(
                createUserRequestDto.getEmail(),
                createUserRequestDto.getPassword(),
                createUserRequestDto.getLanguage()
        );

        UserEmailVerificationEmail userEmailVerificationEmail = new UserEmailVerificationEmail();
        userEmailVerificationEmail.setTo(user.getEmail());
        userEmailVerificationEmail.setUserId(user.getId());
        userEmailVerificationEmail.setLanguage(user.getLanguage());
        emailService.queueEmail(userEmailVerificationEmail);

        UserResponseDto createUserResponseDto = new UserResponseDto();
        createUserResponseDto.setUser(user);

        return createUserResponseDto;
    }

    @PostMapping("/{userId}/email")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public BaseResponseDto updateUserEmail(
            @PathVariable("userId") User user,
            @RequestBody @Valid UpdateUserEmailInitRequestDto updateUserEmailInitRequestDto
    ) {
        if (!Objects.equals(user.getEmail(), updateUserEmailInitRequestDto.getEmail())) {
            User existedUser = userService.getUserByEmail(updateUserEmailInitRequestDto.getEmail());
            if (existedUser != null) {
                throw new IllegalArgumentException("User already exists");
            }

            user.setEmail(updateUserEmailInitRequestDto.getEmail());
            user.setEmailVerified(false);
            userService.saveUser(user);

            UserEmailVerificationEmail userEmailVerificationEmail = new UserEmailVerificationEmail();
            userEmailVerificationEmail.setTo(updateUserEmailInitRequestDto.getEmail());
            userEmailVerificationEmail.setUserId(user.getId());
            userEmailVerificationEmail.setLanguage(user.getLanguage());
            emailService.queueEmail(userEmailVerificationEmail);
        }

        return new BaseResponseDto();
    }

    @PostMapping("/{userId}/password")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public BaseResponseDto updateUserPassword(
            @PathVariable("userId") User user,
            @RequestBody @Valid UpdateUserPasswordRequestDto updateUserPasswordRequestDto
    ) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            user.getEmail(),
                            updateUserPasswordRequestDto.getCurrentPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new AccessDeniedHttpException("Authentication failed, please check your credentials");
        }

        User authenticatedUser = (User) authentication.getPrincipal();
        if (!Objects.equals(authenticatedUser.getId(), user.getId())) {
            throw new AccessDeniedHttpException("Authentication failed, please check your credentials");
        }

        userService.updateUserPassword(
                user,
                updateUserPasswordRequestDto.getNewPassword()
        );

        UserPasswordChangedEmail userPasswordChangedEmail = new UserPasswordChangedEmail();
        userPasswordChangedEmail.setTo(user.getEmail());
        userPasswordChangedEmail.setUserId(user.getId());
        userPasswordChangedEmail.setLanguage(user.getLanguage());
        emailService.queueEmail(userPasswordChangedEmail);

        return new BaseResponseDto();
    }

    @PostMapping("/password/reset/send-email")
    public BaseResponseDto sendResetUserPasswordEmail(
            @RequestBody ResetUserPasswordInitRequestDto resetUserPasswordInitRequestDto
    ) {
        User user = userService.getUserByEmail(resetUserPasswordInitRequestDto.getEmail());
        if (user != null) {
            if (throttlerService.isResetUserPasswordEmailSendRequestThresholdExceeded(user)) {
                throw new TooManyRequestsHttpException("Your client is sending too many requests. Please try again later.");
            }
            throttlerService.incrementResetUserPasswordEmailSendRequest(user);

            UserPasswordResetEmail userPasswordResetEmail = new UserPasswordResetEmail();
            userPasswordResetEmail.setTo(user.getEmail());
            userPasswordResetEmail.setUserId(user.getId());
            userPasswordResetEmail.setLanguage(user.getLanguage());

            emailService.queueEmail(userPasswordResetEmail);
        }

        return new BaseResponseDto();
    }

    @PostMapping("/password/reset")
    public BaseResponseDto resetUserPassword(
            @RequestBody @Valid ResetUserPasswordRequestDto resetUserPasswordRequestDto
    ) throws Exception {
        UserPasswordResetToken token;

        try {
            token = userPasswordResetTokenService.buildToken(resetUserPasswordRequestDto.getToken());
        } catch (Throwable e) {
            throw new AccessDeniedHttpException("Invalid token");
        }

        userService.resetUserPasswordByToken(token, resetUserPasswordRequestDto.getPassword());

        UserPasswordChangedEmail userPasswordChangedEmail = new UserPasswordChangedEmail();
        userPasswordChangedEmail.setTo(token.getUser().getEmail());
        userPasswordChangedEmail.setUserId(token.getUser().getId());
        userPasswordChangedEmail.setLanguage(token.getUser().getLanguage());
        emailService.queueEmail(userPasswordChangedEmail);

        return new BaseResponseDto();
    }

    @PostMapping("/email/verify")
    public BaseResponseDto verifyUserEmail(
            @RequestBody VerifyUserEmailRequestDto verifyUserEmailRequestDto
    ) throws Exception {
        UserEmailVerificationToken token;

        try {
            token = userEmailVerificationTokenService.buildToken(verifyUserEmailRequestDto.getToken());
        } catch (Throwable e) {
            throw new AccessDeniedHttpException("Invalid token");
        }

        userService.verifyUserEmailByToken(token);

        return new BaseResponseDto();
    }

    @PostMapping("/{userId}/email/verification/send-email")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public BaseResponseDto sendUserEmailVerificationEmail(
            @PathVariable("userId") User user
    ) {
        if (user.isEmailVerified()) {
            throw new AccessDeniedHttpException("Email is already verified");
        }

        if (throttlerService.isUserEmailVerificationEmailSendRequestThresholdExceeded(user)) {
            throw new TooManyRequestsHttpException("Your client is sending too many requests. Please try again later.");
        }
        throttlerService.incrementUserEmailVerificationEmailSendRequest(user);

        UserEmailVerificationEmail userEmailVerificationEmail = new UserEmailVerificationEmail();
        userEmailVerificationEmail.setTo(user.getEmail());
        userEmailVerificationEmail.setUserId(user.getId());
        userEmailVerificationEmail.setLanguage(user.getLanguage());
        emailService.queueEmail(userEmailVerificationEmail);

        return new BaseResponseDto();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public BaseResponseDto deleteUser(
            @PathVariable("userId") User user
    ) {
        userService.removeUser(user);

        return new BaseResponseDto();
    }

    @PostMapping("/{userId}/domain/{domainId}/favorite")
    @PreAuthorize(
            "hasRole('USER') && authentication.getPrincipal().getId() == #user.getId() && " +
            "authentication.getPrincipal().getId() == #domain.getUser().getId() && " +
            "#domain.isEnabled() == true"
    )
    public BaseResponseDto favoriteDomain(
            @PathVariable("userId") User user,
            @PathVariable("domainId") AbstractUserDomain domain
    ) {
        user.setFavoriteDomain(domain);
        userService.saveUser(user);

        return new BaseResponseDto();
    }

    @PostMapping("/{userId}/mailbox/{mailboxId}/favorite")
    @PreAuthorize(
            "hasRole('USER') && authentication.getPrincipal().getId() == #user.getId() && " +
            "authentication.getPrincipal().getId() == #mailbox.getUser().getId() && " +
            "#mailbox.isEnabled() == true"
    )
    public BaseResponseDto favoriteMailbox(
            @PathVariable("userId") User user,
            @PathVariable("mailboxId") Mailbox mailbox
    ) {
        user.setFavoriteMailbox(mailbox);
        userService.saveUser(user);

        return new BaseResponseDto();
    }

    @PostMapping("/{userId}/language")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public BaseResponseDto updateUserLanguage(
            @PathVariable("userId") User user,
            @RequestBody UpdateUserLanguageRequestDto updateUserLanguageRequestDto
    ) throws Exception {
        userService.updateUserLanguage(user, updateUserLanguageRequestDto.getLanguage());

        return new BaseResponseDto();
    }
}