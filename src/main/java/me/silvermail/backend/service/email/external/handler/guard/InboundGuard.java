package me.silvermail.backend.service.email.external.handler.guard;

import me.silvermail.backend.entity.Mailbox;
import me.silvermail.backend.entity.alias.MailboxAlias;
import me.silvermail.backend.entity.domain.AbstractUserDomain;
import me.silvermail.backend.exception.email.external.*;
import me.silvermail.backend.model.email.external.ExternalEmail;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class InboundGuard {
    public void check(
            ExternalEmail externalEmail,
            MailboxAlias recipientMailboxAlias,
            AbstractUserDomain recipientDomain
    ) {
        if (recipientDomain == null && recipientMailboxAlias == null) {
            throw new RecipientAliasNotFoundException();
        }

        if (recipientMailboxAlias != null && !recipientMailboxAlias.isEnabled()) {
            throw new RecipientAliasDisabledException();
        }

        Mailbox mailbox = recipientMailboxAlias != null ? recipientMailboxAlias.getMailbox() : recipientDomain.getCatchAllMailbox();
        if (mailbox == null) {
            // there always should be alias' mailbox or catch all mailbox
            throw new RecipientAliasNotFoundException();
        }

        if (!mailbox.isEnabled()) {
            throw new MailboxDisabledException();
        }

        if (!mailbox.isEmailVerified()) {
            throw new MailboxEmailUnverifiedException();
        }

        if (Objects.equals( // disallow if mailbox address is equal to sender
                externalEmail.getSmtpSenderAddress().getAddress(),
                mailbox.getEmail()
        )) {
            throw new EmailSendLoopException();
        }
    }
}
