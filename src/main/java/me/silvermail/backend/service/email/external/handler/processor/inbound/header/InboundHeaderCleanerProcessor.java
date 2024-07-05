package me.silvermail.backend.service.email.external.handler.processor.inbound.header;

import jakarta.mail.MessagingException;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.alias.MailboxAlias;
import me.silvermail.backend.model.email.external.ExternalEmail;
import me.silvermail.backend.service.email.external.HeaderService;
import me.silvermail.backend.service.email.external.handler.processor.inbound.AbstractInboundProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Qualifier("email.external.processor.inbound")
public class InboundHeaderCleanerProcessor extends AbstractInboundProcessor {
    @Autowired
    protected HeaderService headerService;

    public void process(ExternalEmail externalEmail, MailboxAlias recipientMailboxAlias) throws MessagingException {
        if (Objects.equals(recipientMailboxAlias.getUser().getEmailMode(), User.EMAIL_MODE_HARD_REPLACE)) {
            headerService.clearHeaders(externalEmail);
        }
    }
}
