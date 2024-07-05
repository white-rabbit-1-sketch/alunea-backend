package me.silvermail.backend.service.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.repository.TokenRepository;
import me.silvermail.backend.service.KeyService;
import me.silvermail.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import me.silvermail.backend.entity.token.AbstractToken;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
abstract public class AbstractTokenService {
    public static final String CLAIM_TYPE = "type";

    @Autowired
    protected KeyService keyService;

    @Autowired
    protected TokenRepository tokenRepository;

    @Autowired
    @Lazy
    protected UserService userService;

    @Value("${token.private-key}")
    protected String tokenPrivateKey;

    @Value("${token.public-key}")
    protected String tokenPublicKey;



    protected AbstractToken createToken(
            User user,
            Map<String, String> claimList
    ) throws Exception {
        AbstractToken token = buildTokenEntity(user, claimList);
        tokenRepository.save(token);

        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(token.getId())
                .setSubject(user.getId())
                .setExpiration(token.getExpireTime())
                .setIssuedAt(token.getCreateTime())
                .claim(CLAIM_TYPE, getTokenType());
        for (Map.Entry<String, String> claim : claimList.entrySet()) {
            jwtBuilder.claim(claim.getKey(), claim.getValue());
        }

        token.setValue(jwtBuilder.signWith(keyService.buildPrivateKey(tokenPrivateKey)).compact());

        return token;
    }

    protected AbstractToken buildTokenEntity(
            User user,
            Map<String, String> claimList
    ) throws Exception {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(getTokenTtl());

        Date createTime = Date.from(now.truncatedTo(ChronoUnit.SECONDS));
        Date expireTime = Date.from(expiration.truncatedTo(ChronoUnit.SECONDS));

        AbstractToken token = getTokenClas().getDeclaredConstructor().newInstance();
        token.setUser(user);
        token.setType(getTokenType());
        token.setExpireTime(expireTime);
        token.setCreateTime(createTime);

        return token;
    }

    protected Claims getTokenClaims(String value) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(keyService.buildPublicKey(tokenPublicKey))
                .build()
                .parseClaimsJws(value)
                .getBody();
    }

    protected AbstractToken buildBaseToken(String value, Claims claims) throws Exception {
        AbstractToken token = tokenRepository.findById(claims.getId()).orElseThrow();

        token.setValue(value);

        if (!Objects.equals(token.getType(), getTokenType())) {
            throw new Exception("Token type mismatch");
        }

        return token;
    }

    public void saveToken(AbstractToken token) throws Exception {
        tokenRepository.save(token);
    }

    public void removeToken(AbstractToken token) throws Exception {
        tokenRepository.delete(token);
    }

    abstract protected String getTokenType();
    abstract protected Integer getTokenTtl();
    abstract protected Class<? extends AbstractToken> getTokenClas();
    abstract protected AbstractToken buildToken(String value) throws Exception;
}
