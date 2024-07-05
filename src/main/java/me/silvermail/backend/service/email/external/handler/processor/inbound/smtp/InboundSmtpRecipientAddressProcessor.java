package me.silvermail.backend.service.email.external.handler.processor.inbound.smtp;

import jakarta.mail.MessagingException;
import me.silvermail.backend.entity.alias.MailboxAlias;
import me.silvermail.backend.model.email.external.ExternalEmail;
import me.silvermail.backend.service.email.external.handler.processor.inbound.AbstractInboundProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("email.external.processor.inbound")
public class InboundSmtpRecipientAddressProcessor extends AbstractInboundProcessor {
    public void process(ExternalEmail externalEmail, MailboxAlias recipientMailboxAlias) throws MessagingException {
        externalEmail.getSmtpRecipientAddress().setAddress(recipientMailboxAlias.getMailbox().getEmail());
    }
}
