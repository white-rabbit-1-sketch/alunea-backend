package me.silvermail.backend.service.email.external.handler.guard;

import me.silvermail.backend.entity.alias.ContactAlias;
import me.silvermail.backend.exception.email.external.*;
import me.silvermail.backend.model.email.external.ExternalEmail;
import me.silvermail.backend.service.ThrottlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OutboundGuard {
    @Autowired
    protected ThrottlerService throttlerService;

    public void check(
            ExternalEmail externalEmail,
            ContactAlias recipientContactAlias
    ) {
        if (recipientContactAlias == null) {
            throw new RecipientAliasNotFoundException();
        }

        if (!recipientContactAlias.isEnabled()) {
            throw new RecipientAliasDisabledException();
        }

        if (!Objects.equals(
                recipientContactAlias.getMailboxAlias().getMailbox().getEmail(),
                externalEmail.getSmtpSenderAddress().getAddress()
        )) {
            throw new InvalidContactAliasException();
        }

        if (Objects.equals( // disallow if contact address is equal to sender
                recipientContactAlias.getContact().getEmail(),
                externalEmail.getSmtpSenderAddress().getAddress()
        )) {
            throw new EmailSendLoopException();
        }

        if (throttlerService.isSentOutboundExternalEmailThresholdExceeded(recipientContactAlias.getUser())) {
            throw new EmailSentThresholdExceededException();
        }
    }
}
