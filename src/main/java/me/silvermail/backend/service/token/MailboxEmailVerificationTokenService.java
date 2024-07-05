package me.silvermail.backend.service.token;

import io.jsonwebtoken.Claims;
import me.silvermail.backend.entity.Mailbox;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.token.AbstractToken;
import me.silvermail.backend.entity.token.MailboxEmailVerificationToken;
import me.silvermail.backend.service.MailboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MailboxEmailVerificationTokenService extends AbstractTokenService {
    public static final String TYPE_MAILBOX_EMAIL_VERIFICATION = "mailbox-email-verification";
    public static final String CLAIM_MAILBOX_ID = "mailboxId";

    @Autowired
    @Lazy
    protected MailboxService mailboxService;

    @Value("${token.mailbox-email-verification.ttl}")
    protected Integer mailboxEmailVerificationTokenTtl;

    @Override
    protected Class<? extends AbstractToken> getTokenClas() {
        return MailboxEmailVerificationToken.class;
    }

    @Override
    protected String getTokenType() {
        return TYPE_MAILBOX_EMAIL_VERIFICATION;
    }

    @Override
    protected Integer getTokenTtl() {
        return mailboxEmailVerificationTokenTtl;
    }

    @Override
    public MailboxEmailVerificationToken buildToken(String value) throws Exception {
        Claims claims = getTokenClaims(value);

        return (MailboxEmailVerificationToken) buildBaseToken(value, claims);
    }

    public MailboxEmailVerificationToken createMailboxEmailVerificationToken(
            Mailbox mailbox
    ) throws Exception {
        return (MailboxEmailVerificationToken) createToken(mailbox.getUser(), new HashMap<>(Map.of(
                CLAIM_MAILBOX_ID, mailbox.getId()
        )));
    }

    @Override
    protected AbstractToken buildTokenEntity(User user, Map<String, String> claimList) throws Exception {
        MailboxEmailVerificationToken mailboxEmailVerificationToken = (MailboxEmailVerificationToken) super.buildTokenEntity(user, claimList);

        Mailbox mailbox = mailboxService.getMailboxById(claimList.get(CLAIM_MAILBOX_ID));

        mailboxEmailVerificationToken.setMailbox(mailbox);

        return mailboxEmailVerificationToken;
    }
}
