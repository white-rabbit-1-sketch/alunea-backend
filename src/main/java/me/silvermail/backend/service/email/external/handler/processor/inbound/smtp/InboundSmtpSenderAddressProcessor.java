package me.silvermail.backend.service.email.external.handler.processor.inbound.smtp;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.alias.MailboxAlias;
import me.silvermail.backend.model.email.external.ExternalEmail;
import me.silvermail.backend.service.email.external.handler.processor.inbound.AbstractInboundAddressProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Qualifier("email.external.processor.inbound")
public class InboundSmtpSenderAddressProcessor extends AbstractInboundAddressProcessor {
    public void process(ExternalEmail externalEmail, MailboxAlias recipientMailboxAlias) throws MessagingException {
        log.debug("Email mode: {}", recipientMailboxAlias.getUser().getEmailMode());

        if (Objects.equals(recipientMailboxAlias.getUser().getEmailMode(), User.EMAIL_MODE_HARD_REPLACE)) {
            replaceAddressListByContactAliasList(
                    List.of(externalEmail.getSmtpSenderAddress()),
                    recipientMailboxAlias,
                    false
            );
        }
    }
}
