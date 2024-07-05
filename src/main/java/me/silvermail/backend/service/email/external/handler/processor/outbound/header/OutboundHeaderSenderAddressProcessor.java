package me.silvermail.backend.service.email.external.handler.processor.outbound.header;

import jakarta.mail.MessagingException;
import me.silvermail.backend.entity.alias.ContactAlias;
import me.silvermail.backend.model.email.external.ExternalEmail;
import me.silvermail.backend.service.email.external.handler.processor.outbound.AbstractOutboundProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("email.external.processor.outbound")
public class OutboundHeaderSenderAddressProcessor extends AbstractOutboundProcessor {
    public void process(ExternalEmail externalEmail, ContactAlias recipientContactAlias) throws MessagingException {
        externalEmail.getMimeMessage().setFrom(recipientContactAlias.getMailboxAlias().getValue());
    }
}
