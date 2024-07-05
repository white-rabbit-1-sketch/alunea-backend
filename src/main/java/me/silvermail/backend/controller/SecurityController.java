package me.silvermail.backend.controller;

import me.silvermail.backend.dto.controller.request.security.TokenRequestDto;
import me.silvermail.backend.dto.controller.response.security.TokenResponseDto;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.exception.http.TooManyRequestsHttpException;
import me.silvermail.backend.exception.http.UnauthorizedHttpException;
import me.silvermail.backend.service.ThrottlerService;
import me.silvermail.backend.service.UserService;
import me.silvermail.backend.service.token.UserAuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/security")
public class SecurityController {
    @Autowired
    protected AuthenticationManager authenticationManager;
    @Autowired
    protected UserAuthTokenService userAuthTokenService;
    @Autowired
    protected ThrottlerService throttlerService;
    @Autowired
    protected UserService userService;

    @PostMapping("/user/auth-token")
    public TokenResponseDto createUserAuthToken(@RequestBody TokenRequestDto tokenRequestDto) throws Exception {
        User user = userService.getUserByEmail(tokenRequestDto.getEmail());
        if (user != null) {
            if (throttlerService.isUserAuthTokenCreateRequestThresholdExceeded(user)) {
                throw new TooManyRequestsHttpException("Your client is sending too many requests. Please try again later.");
            }
            throttlerService.incrementUserAuthTokenCreateRequest(user);
        }

        Authentication authentication = null;

        try {
            authentication = this.authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            tokenRequestDto.getEmail(),
                            tokenRequestDto.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new UnauthorizedHttpException("Authentication failed, please check your credentials");
        }

        user = (User) authentication.getPrincipal();

        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setToken(userAuthTokenService.createUserAuthToken(user).getValue());

        return tokenResponseDto;
    }
}