package me.silvermail.backend.service.email.external.handler.processor.outbound.header;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import me.silvermail.backend.entity.alias.AbstractAlias;
import me.silvermail.backend.entity.alias.ContactAlias;
import me.silvermail.backend.model.email.external.ExternalEmail;
import me.silvermail.backend.service.AliasService;
import me.silvermail.backend.service.email.external.handler.processor.outbound.AbstractOutboundProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Qualifier("email.external.processor.outbound")
public class OutboundHeaderRecipientAddressProcessor extends AbstractOutboundProcessor {
    protected static final List<Message.RecipientType> RECIPIENT_TYPE_LIST = List.of(
            Message.RecipientType.TO,
            Message.RecipientType.CC,
            Message.RecipientType.BCC
    );

    @Autowired
    protected AliasService aliasService;

    public void process(ExternalEmail externalEmail, ContactAlias recipientContactAlias) throws MessagingException {
        for (Message.RecipientType recipientType : RECIPIENT_TYPE_LIST) {
            Address[] recipientArray = externalEmail.getMimeMessage().getRecipients(recipientType);
            if (recipientArray == null || recipientArray.length == 0) {
                continue;
            }

            Map<String, InternetAddress> recipientAddressMap = Arrays.stream(recipientArray)
                    .map(address -> (InternetAddress) address)
                    .collect(Collectors.toMap(InternetAddress::getAddress, address -> address));
            Map<String, AbstractAlias> recipientContactAliasMap = aliasService.getAliasMapByValueList(
                    recipientAddressMap.keySet().stream().toList()
            );
            for (Map.Entry<String, AbstractAlias> entry : recipientContactAliasMap.entrySet()) {
                String email = entry.getKey();
                AbstractAlias alias = entry.getValue();

                if (
                        !Objects.equals(alias.getUser().getId(), recipientContactAlias.getUser().getId()) ||
                                !Objects.equals(alias.getType(), ContactAlias.TYPE)
                ) {
                    continue;
                }

                recipientAddressMap.get(email).setAddress(((ContactAlias)alias).getContact().getEmail());
            }

            externalEmail.getMimeMessage().setRecipients(recipientType, recipientArray);
        }
    }
}
