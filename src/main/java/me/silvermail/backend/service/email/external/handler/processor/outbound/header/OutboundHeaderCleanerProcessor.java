package me.silvermail.backend.service.email.external.handler.processor.outbound.header;

import jakarta.mail.MessagingException;
import me.silvermail.backend.entity.alias.ContactAlias;
import me.silvermail.backend.model.email.external.ExternalEmail;
import me.silvermail.backend.service.email.external.HeaderService;
import me.silvermail.backend.service.email.external.handler.processor.outbound.AbstractOutboundProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("email.external.processor.outbound")
public class OutboundHeaderCleanerProcessor extends AbstractOutboundProcessor {
    @Autowired
    protected HeaderService headerService;

    public void process(ExternalEmail externalEmail, ContactAlias recipientContactAlias) throws MessagingException {
        headerService.clearHeaders(externalEmail);
    }
}
