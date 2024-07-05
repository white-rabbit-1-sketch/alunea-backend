package me.silvermail.backend.service.token;

import io.jsonwebtoken.Claims;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.token.AbstractToken;
import me.silvermail.backend.entity.token.UserEmailVerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserEmailVerificationTokenService extends AbstractTokenService {
    public static final String TYPE_USER_EMAIL_VERIFICATION = "user-email-verification";
    public static final String CLAIM_EMAIL = "email";

    @Value("${token.user-email-verification.ttl}")
    protected Integer userEmailVerificationTokenTtl;

    @Override
    protected Class<? extends AbstractToken> getTokenClas() {
        return UserEmailVerificationToken.class;
    }

    @Override
    protected String getTokenType() {
        return TYPE_USER_EMAIL_VERIFICATION;
    }

    @Override
    protected Integer getTokenTtl() {
        return userEmailVerificationTokenTtl;
    }

    @Override
    public UserEmailVerificationToken buildToken(String value) throws Exception {
        Claims claims = getTokenClaims(value);

        return (UserEmailVerificationToken) buildBaseToken(value, claims);
    }

    public UserEmailVerificationToken createUserEmailVerificationToken(
            User user,
            String email
    ) throws Exception {
        return (UserEmailVerificationToken) createToken(user, new HashMap<>(Map.of(
                CLAIM_EMAIL, email
        )));
    }

    @Override
    protected AbstractToken buildTokenEntity(User user, Map<String, String> claimList) throws Exception {
        UserEmailVerificationToken userEmailVerificationToken = (UserEmailVerificationToken) super.buildTokenEntity(user, claimList);

        userEmailVerificationToken.setEmail(claimList.get(CLAIM_EMAIL));

        return userEmailVerificationToken;
    }
}
