package me.silvermail.backend.service.email.external.handler.processor.inbound.header;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.alias.MailboxAlias;
import me.silvermail.backend.model.email.external.ExternalEmail;
import me.silvermail.backend.service.AliasService;
import me.silvermail.backend.service.ContactService;
import me.silvermail.backend.service.email.external.handler.processor.inbound.AbstractInboundAddressProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Qualifier("email.external.processor.inbound")
public class InboundHeaderRecipientAddressProcessor extends AbstractInboundAddressProcessor {
    protected static final List<Message.RecipientType> RECIPIENT_TYPE_LIST = List.of(
            Message.RecipientType.TO,
            Message.RecipientType.CC,
            Message.RecipientType.BCC
    );

    @Autowired
    protected AliasService aliasService;
    @Autowired
    protected ContactService contactService;

    public void process(ExternalEmail externalEmail, MailboxAlias recipientMailboxAlias) throws MessagingException {
        if (Objects.equals(recipientMailboxAlias.getUser().getEmailMode(), User.EMAIL_MODE_HARD_REPLACE)) {
            for (Message.RecipientType recipientType : RECIPIENT_TYPE_LIST) {
                Address[] recipientArray = externalEmail.getMimeMessage().getRecipients(recipientType);
                if (recipientArray == null || recipientArray.length == 0) {
                    continue;
                }

                List<InternetAddress> recipientList = Arrays.stream(recipientArray)
                        .map(address -> (InternetAddress) address)
                        .toList();

                replaceAddressListByContactAliasList(recipientList, recipientMailboxAlias, true);
                externalEmail.getMimeMessage().setRecipients(recipientType, recipientList.toArray(new Address[0]));
            }
        }
    }
}
