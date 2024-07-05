package me.silvermail.backend.service.email.external.handler.processor.inbound.header;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.alias.MailboxAlias;
import me.silvermail.backend.model.email.external.ExternalEmail;
import me.silvermail.backend.service.AliasService;
import me.silvermail.backend.service.ContactService;
import me.silvermail.backend.service.email.external.handler.processor.inbound.AbstractInboundAddressProcessor;
import me.silvermail.backend.service.email.external.handler.processor.outbound.header.OutboundHeaderReplyToAddressProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Qualifier("email.external.processor.inbound")
public class InboundHeaderReplyToAddressProcessor extends AbstractInboundAddressProcessor {
    @Autowired
    protected AliasService aliasService;
    @Autowired
    protected ContactService contactService;
    @Autowired
    private OutboundHeaderReplyToAddressProcessor replyToAddressProcessor;

    public void process(ExternalEmail externalEmail, MailboxAlias recipientMailboxAlias) throws MessagingException, UnsupportedEncodingException {
        if (
                Objects.equals(recipientMailboxAlias.getUser().getEmailMode(), User.EMAIL_MODE_HARD_REPLACE) ||
                Objects.equals(recipientMailboxAlias.getUser().getEmailMode(), User.EMAIL_MODE_SOFT_REPLACE)
        ) {
            InternetAddress headerSenderAddress = (InternetAddress) Arrays.stream(externalEmail.getMimeMessage().getFrom())
                    .map(address -> (InternetAddress) address)
                    .toList().getFirst().clone();

            replaceAddressListByContactAliasList(
                    List.of(headerSenderAddress),
                    recipientMailboxAlias,
                    true
            );

            externalEmail.getMimeMessage().setReplyTo(new InternetAddress[]{headerSenderAddress});
        }
    }
}
