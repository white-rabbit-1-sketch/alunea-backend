package me.silvermail.backend.service.email.external.handler.processor.inbound;

import jakarta.mail.internet.InternetAddress;
import lombok.extern.slf4j.Slf4j;
import me.silvermail.backend.entity.Contact;
import me.silvermail.backend.entity.alias.AbstractAlias;
import me.silvermail.backend.entity.alias.ContactAlias;
import me.silvermail.backend.entity.alias.MailboxAlias;
import me.silvermail.backend.service.AliasService;
import me.silvermail.backend.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
abstract public class AbstractInboundAddressProcessor extends AbstractInboundProcessor {
    @Autowired
    protected AliasService aliasService;
    @Autowired
    protected ContactService contactService;

    protected void replaceAddressListByContactAliasList(
            List<InternetAddress> addressList,
            MailboxAlias mailboxAlias,
            boolean keepOwnedMailboxAliases
    ) {
        log.debug("Starting replacing address list by contact aliases");
        Map<String, InternetAddress> addressMap = addressList
                .stream()
                .collect(Collectors.toMap(InternetAddress::getAddress, address -> address));
        Map<String, AbstractAlias> aliasMap = aliasService.getAliasMapByValueList(
                addressMap.keySet().stream().toList()
        );
        Map<String, Contact> contactMap = contactService.getContactMapByUserIdAndEmailList(
                mailboxAlias.getUser().getId(),
                addressMap.keySet().stream().toList()
        );

        for (Map.Entry<String, InternetAddress> entry : addressMap.entrySet()) {
            String email = entry.getKey();
            InternetAddress internetAddress = entry.getValue();
            AbstractAlias alias = aliasMap.get(email);

            if (
                    alias != null &&
                    (
                        Objects.equals(alias.getType(), ContactAlias.TYPE) ||
                        (
                            keepOwnedMailboxAliases &&
                            Objects.equals(alias.getType(), MailboxAlias.TYPE) &&
                            Objects.equals(alias.getUser().getId(), mailboxAlias.getUser().getId())
                        )
                    )
            ) {
                log.debug("Skipping replacing address {} list by contact aliases", internetAddress.getAddress());
                continue;
            }

            Contact contact = contactMap.get(email);
            if (contact == null) {
                log.debug("Creating contact {}", internetAddress.getAddress());
                contact = contactService.createContact(
                        mailboxAlias.getUser(),
                        internetAddress.getAddress()
                );
                contactMap.put(contact.getEmail(), contact);
            }

            ContactAlias contactAlias = null;
            List<ContactAlias> contactAliasList = contact.getContactAliasList().stream()
                    .filter(a -> a.getMailboxAlias().getId().equals(mailboxAlias.getId()))
                    .toList();
            if (!contactAliasList.isEmpty()) {
                contactAlias = contactAliasList.getFirst();
            }
            if (contactAlias == null) {
                contactAlias = aliasService.createRandomContactAlias(
                        contact,
                        mailboxAlias
                );
            }

            log.debug("Address {} was replaced to contact alias: {}", internetAddress.getAddress(), contactAlias);
            internetAddress.setAddress(contactAlias.getValue());
        }
    }
}
