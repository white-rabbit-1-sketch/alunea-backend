package me.silvermail.backend.service.token;

import io.jsonwebtoken.Claims;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.token.AbstractToken;
import me.silvermail.backend.entity.token.UserPasswordResetToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserPasswordResetTokenService extends AbstractTokenService {
    public static final String TYPE_USER_PASSWORD_RESET = "user-password-reset";

    @Value("${token.user-password-reset.ttl}")
    protected Integer userPasswordResetTokenTtl;

    @Override
    protected Class<? extends AbstractToken> getTokenClas() {
        return UserPasswordResetToken.class;
    }

    @Override
    protected String getTokenType() {
        return TYPE_USER_PASSWORD_RESET;
    }

    @Override
    protected Integer getTokenTtl() {
        return userPasswordResetTokenTtl;
    }

    @Override
    public UserPasswordResetToken buildToken(String value) throws Exception {
        Claims claims = getTokenClaims(value);

        return (UserPasswordResetToken) buildBaseToken(value, claims);
    }

    public UserPasswordResetToken createUserPasswordResetToken(User user) throws Exception {
        return (UserPasswordResetToken) createToken(user, new HashMap<>());
    }
}
