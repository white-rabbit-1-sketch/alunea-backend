package me.silvermail.backend.service.token;

import io.jsonwebtoken.Claims;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.token.AbstractToken;
import me.silvermail.backend.entity.token.UserAuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserAuthTokenService extends AbstractTokenService {
    public static final String TYPE_USER_AUTH = "user-auth";

    @Value("${token.user-auth.ttl}")
    protected Integer userAuthTokenTtl;

    @Override
    protected String getTokenType() {
        return TYPE_USER_AUTH;
    }

    @Override
    protected Integer getTokenTtl() {
        return userAuthTokenTtl;
    }

    @Override
    protected Class<? extends AbstractToken> getTokenClas() {
        return UserAuthToken.class;
    }

    @Override
    public UserAuthToken buildToken(String value) throws Exception {
        Claims claims = getTokenClaims(value);

        return (UserAuthToken) buildBaseToken(value, claims);
    }

    public UserAuthToken createUserAuthToken(User user) throws Exception {
        return (UserAuthToken) createToken(user, new HashMap<>());
    }
}
