package me.silvermail.backend.service;

import me.silvermail.backend.entity.Contact;
import me.silvermail.backend.entity.Mailbox;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.alias.AbstractAlias;
import me.silvermail.backend.entity.alias.ContactAlias;
import me.silvermail.backend.entity.alias.MailboxAlias;
import me.silvermail.backend.entity.domain.AbstractDomain;
import me.silvermail.backend.entity.domain.AbstractUserDomain;
import me.silvermail.backend.entity.domain.Subdomain;
import me.silvermail.backend.repository.AliasRepository;
import me.silvermail.backend.repository.ContactAliasRepository;
import me.silvermail.backend.repository.MailboxAliasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AliasService {
    private static final int CONTACT_ALIAS_RANDOM_LENGTH = 8;

    @Autowired
    protected AliasRepository aliasRepository;
    @Autowired
    protected MailboxAliasRepository mailboxAliasRepository;
    @Autowired
    protected ContactAliasRepository contactAliasRepository;
    @Autowired
    protected DomainService domainService;
    @Autowired
    protected StringService stringService;

    public AbstractAlias getAliasById(String id) {
        return aliasRepository.findById(id).orElse(null);
    }

    public AbstractAlias getAliasByValue(
            String value
    ) {
        return aliasRepository.findByValue(value);
    }

    public MailboxAlias getMailboxAliasByAliasValue(
            String aliasValue
    ) {
        AbstractAlias alias = aliasRepository.findByValue(aliasValue);

        return alias != null && Objects.equals(alias.getType(), MailboxAlias.TYPE) ? (MailboxAlias) alias : null;
    }

    public ContactAlias getContactAliasByAliasValue(
            String aliasValue
    ) {
        AbstractAlias alias = aliasRepository.findByValue(aliasValue);

        return alias != null && Objects.equals(alias.getType(), ContactAlias.TYPE) ? (ContactAlias) alias : null;
    }

    public List<AbstractAlias> getAliasListByValueList(List<String> valueList) {
        return aliasRepository.findByValueList(valueList);
    }

    public Map<String, AbstractAlias> getAliasMapByValueList(List<String> valueList) {
        return getAliasListByValueList(valueList)
                .stream()
                .collect(Collectors.toMap(AbstractAlias::getValue, alias -> alias));
    }


    public AbstractAlias getAliasByDomainIdAndRecipient(
            String domainId,
            String recipient
    ) {
        return aliasRepository.findByDomainIdAndRecipient(domainId, recipient);
    }

    public List<MailboxAlias> getMailboxAliasListByUserId(String userId) {
        return mailboxAliasRepository.findAllByUserId(userId);
    }

    public List<ContactAlias> getContactAliasListByUserId(String userId) {
        return contactAliasRepository.findAllByUserId(userId);
    }

    public ContactAlias getContactAliasByContactIdAndMailboxAliasId(
            String contactId,
            String mailboxAliasId
    ) {
        return contactAliasRepository.findByContactIdAndMailboxAliasId(contactId, mailboxAliasId);
    }

    public MailboxAlias createMailboxAlias(
            User user,
            Mailbox mailbox,
            AbstractUserDomain domain,
            String recipient
    ) {
        recipient = recipient.trim();

        if (
                !Objects.equals(mailbox.getUser().getId(), user.getId()) ||
                !Objects.equals(domain.getUser().getId(), user.getId())
        ) {
            throw new IllegalArgumentException("User mismatch");
        }

        AbstractAlias existedAlias = getAliasByDomainIdAndRecipient(
                domain.getId(),
                recipient
        );
        if (existedAlias != null) {
            throw new IllegalArgumentException("Alias already exists");
        }

        MailboxAlias mailboxAlias = new MailboxAlias();
        mailboxAlias.setType(MailboxAlias.TYPE);
        mailboxAlias.setUser(user);
        mailboxAlias.setMailbox(mailbox);
        mailboxAlias.setDomain(domain);
        mailboxAlias.setRecipient(recipient);
        mailboxAlias.setValue(buildAliasValue(recipient, domain.getDomain()));
        mailboxAlias.setCreateTime(new Date());

        mailboxAliasRepository.save(mailboxAlias);

        return mailboxAlias;
    }

    public ContactAlias createContactAlias(
            User user,
            Contact contact,
            MailboxAlias mailboxAlias,
            String recipient
    ) {
        recipient = recipient.trim();

        if (
                !Objects.equals(contact.getUser().getId(), user.getId()) ||
                !Objects.equals(mailboxAlias.getUser().getId(), user.getId())
        ) {
            throw new IllegalArgumentException("User mismatch");
        }

        AbstractAlias existedAlias = getAliasByDomainIdAndRecipient(
                mailboxAlias.getDomain().getId(),
                recipient
        );
        if (existedAlias != null) {
            throw new IllegalArgumentException("Alias already exists");
        }

        ContactAlias contactAlias = new ContactAlias();
        contactAlias.setType(ContactAlias.TYPE);
        contactAlias.setUser(user);
        contactAlias.setContact(contact);
        contactAlias.setMailboxAlias(mailboxAlias);
        contactAlias.setDomain(mailboxAlias.getDomain());
        contactAlias.setRecipient(recipient);
        contactAlias.setValue(buildAliasValue(recipient, mailboxAlias.getDomain().getDomain()));
        contactAlias.setCreateTime(new Date());

        contactAliasRepository.save(contactAlias);

        return contactAlias;
    }

    public ContactAlias createRandomContactAlias(
            Contact contact,
            MailboxAlias mailboxAlias
    ) {
        ContactAlias contactAlias = createContactAlias(
                mailboxAlias.getUser(),
                contact,
                mailboxAlias,
                buildContactAliasRecipient(contact)
        );
        contact.getContactAliasList().add(contactAlias);

        return contactAlias;
    }

    public String buildContactAliasRecipient(Contact contact) {
        return contact.getEmail().replace("@", "_at_") + "_" + stringService.generateRandomString(CONTACT_ALIAS_RANDOM_LENGTH);
    }

    public void saveAlias(AbstractAlias alias) {
        aliasRepository.save(alias);
    }

    public void removeAlias(AbstractAlias alias) {
        aliasRepository.delete(alias);
    }

    public String buildAliasValue(String recipient, String domain) {
        return recipient + '@' + domain;
    }
}
