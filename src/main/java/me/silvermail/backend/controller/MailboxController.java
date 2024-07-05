package me.silvermail.backend.controller;

import jakarta.validation.Valid;
import me.silvermail.backend.dto.controller.request.mailbox.VerifyMailboxEmailRequestDto;
import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.dto.controller.request.mailbox.CreateMailboxRequestDto;
import me.silvermail.backend.dto.controller.response.mailbox.MailboxListResponseDto;
import me.silvermail.backend.dto.controller.response.mailbox.MailboxResponseDto;
import me.silvermail.backend.dto.controller.response.mailbox.UserMailboxEmailAvailabilityResponseDto;
import me.silvermail.backend.exception.http.BadRequestHttpException;
import me.silvermail.backend.exception.http.TooManyRequestsHttpException;
import me.silvermail.backend.model.email.internal.MailboxEmailVerificationEmail;
import me.silvermail.backend.entity.Mailbox;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.token.MailboxEmailVerificationToken;
import me.silvermail.backend.exception.http.AccessDeniedHttpException;
import me.silvermail.backend.service.InternalEmailService;
import me.silvermail.backend.service.MailboxService;
import me.silvermail.backend.service.ThrottlerService;
import me.silvermail.backend.service.UserService;
import me.silvermail.backend.service.token.MailboxEmailVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MailboxController {
    @Autowired
    protected MailboxService mailboxService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected InternalEmailService emailService;
    @Autowired
    protected MailboxEmailVerificationTokenService mailboxEmailVerificationTokenService;
    @Autowired
    protected ThrottlerService throttlerService;

    @GetMapping(path="/user/{userId}/mailbox/{email}")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public UserMailboxEmailAvailabilityResponseDto isUserMailboxEmailAvailable(
            @PathVariable("userId") User user,
            @PathVariable String email
    ) {
        Mailbox mailbox = this.mailboxService.getMailboxByUserIdAndEmail(user.getId(), email);

        UserMailboxEmailAvailabilityResponseDto userMailboxEmailAvailabilityResponseDto = new UserMailboxEmailAvailabilityResponseDto();
        userMailboxEmailAvailabilityResponseDto.setAvailable(mailbox == null);

        return userMailboxEmailAvailabilityResponseDto;
    }

    @PostMapping("/user/{userId}/mailbox")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public MailboxResponseDto createMailbox(
            @PathVariable("userId") User user,
            @RequestBody @Valid CreateMailboxRequestDto createMailboxRequestDto
    ) {
        if (user.getMailboxList().size() >= 1) {
            throw new BadRequestHttpException("You have reached maximum mailboxes count");
        }

        Mailbox mailbox = mailboxService.createMailbox(user, createMailboxRequestDto.getEmail());

        MailboxEmailVerificationEmail mailboxEmailVerificationEmail = new MailboxEmailVerificationEmail();
        mailboxEmailVerificationEmail.setTo(mailbox.getEmail());
        mailboxEmailVerificationEmail.setMailboxId(mailbox.getId());
        mailboxEmailVerificationEmail.setLanguage(mailbox.getUser().getLanguage());
        emailService.queueEmail(mailboxEmailVerificationEmail);

        MailboxResponseDto mailboxResponseDto = new MailboxResponseDto();
        mailboxResponseDto.setMailbox(mailbox);

        return mailboxResponseDto;
    }

    @GetMapping(path="/user/{userId}/mailbox")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public MailboxListResponseDto getMailboxListByUserId(
            @PathVariable("userId") User user
    ) {
        List<Mailbox> mailboxList = this.mailboxService.getMailboxListByUserId(user.getId());

        MailboxListResponseDto mailboxListResponseDto = new MailboxListResponseDto();
        mailboxListResponseDto.setMailboxList(mailboxList);

        return mailboxListResponseDto;
    }

    @PostMapping("/mailbox/{mailboxId}/enable")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #mailbox.getUser().getId()")
    public BaseResponseDto enableMailbox(
            @PathVariable("mailboxId") Mailbox mailbox
    ) {
        mailbox.setEnabled(true);
        mailboxService.saveMailbox(mailbox);

        return new BaseResponseDto();
    }

    @PostMapping("/mailbox/{mailboxId}/disable")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #mailbox.getUser().getId()")
    public BaseResponseDto disableMailbox(
            @PathVariable("mailboxId") Mailbox mailbox
    ) {
        mailbox.setEnabled(false);
        mailboxService.saveMailbox(mailbox);

        return new BaseResponseDto();
    }

    @DeleteMapping("/mailbox/{mailboxId}")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #mailbox.getUser().getId()")
    public BaseResponseDto removeMailbox(
            @PathVariable("mailboxId") Mailbox mailbox
    ) {
        mailboxService.removeMailbox(mailbox);

        return new BaseResponseDto();
    }

    @PostMapping("/mailbox/{mailboxId}/email/verification/send-email")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #mailbox.getUser().getId()")
    public BaseResponseDto sendMailboxEmailVerificationEmail(
            @PathVariable("mailboxId") Mailbox mailbox
    ) {
        if (mailbox.isEmailVerified()) {
            throw new AccessDeniedHttpException("Email is already verified");
        }

        if (throttlerService.isMailboxEmailVerificationEmailSendRequestThresholdExceeded(mailbox.getUser())) {
            throw new TooManyRequestsHttpException("Your client is sending too many requests. Please try again later.");
        }
        throttlerService.incrementMailboxEmailVerificationEmailSendRequest(mailbox.getUser());

        MailboxEmailVerificationEmail mailboxEmailVerificationEmail = new MailboxEmailVerificationEmail();
        mailboxEmailVerificationEmail.setTo(mailbox.getEmail());
        mailboxEmailVerificationEmail.setMailboxId(mailbox.getId());
        mailboxEmailVerificationEmail.setLanguage(mailbox.getUser().getLanguage());
        emailService.queueEmail(mailboxEmailVerificationEmail);

        return new BaseResponseDto();
    }

    @PostMapping("/mailbox/email/verify")
    public BaseResponseDto verifyMailboxEmail(
            @RequestBody VerifyMailboxEmailRequestDto verifyMailboxEmailRequestDto
    ) throws Exception {
        MailboxEmailVerificationToken token;

        try {
            token = mailboxEmailVerificationTokenService.buildToken(verifyMailboxEmailRequestDto.getToken());
        } catch (Throwable e) {
            throw new AccessDeniedHttpException("Invalid token");
        }

        mailboxService.verifyMailboxEmailByToken(token);

        return new BaseResponseDto();
    }
}