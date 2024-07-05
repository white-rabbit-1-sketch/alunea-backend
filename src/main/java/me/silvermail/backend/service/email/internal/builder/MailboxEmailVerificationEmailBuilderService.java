package me.silvermail.backend.service.email.internal.builder;

import me.silvermail.backend.model.email.internal.AbstractInternalEmail;
import me.silvermail.backend.model.email.internal.MailboxEmailVerificationEmail;
import me.silvermail.backend.entity.Mailbox;
import me.silvermail.backend.entity.token.MailboxEmailVerificationToken;
import me.silvermail.backend.service.MailboxService;
import me.silvermail.backend.service.token.MailboxEmailVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class MailboxEmailVerificationEmailBuilderService extends AbstractEmailBuilderService {
    @Autowired
    MailboxEmailVerificationTokenService mailboxEmailVerificationTokenService;
    @Autowired
    MailboxService mailboxService;

    @Override
    public void build(AbstractInternalEmail email) throws Exception {
        if (!(email instanceof MailboxEmailVerificationEmail mailboxEmailVerificationEmail)) {
            throw new IllegalArgumentException("Invalid email type");
        }

        Mailbox mailbox = mailboxService.getMailboxById(mailboxEmailVerificationEmail.getMailboxId());

        MailboxEmailVerificationToken mailboxEmailVerificationToken = mailboxEmailVerificationTokenService.createMailboxEmailVerificationToken(
                mailbox
        );

        Context context = new Context();
        context.setVariable("mailboxEmailVerificationToken", mailboxEmailVerificationToken.getValue());

        render(mailboxEmailVerificationEmail, context);
    }
}
