package me.silvermail.backend.service.email.external.handler;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import me.silvermail.backend.entity.Contact;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.alias.AbstractAlias;
import me.silvermail.backend.entity.alias.ContactAlias;
import me.silvermail.backend.exception.email.external.EmailSendLoopException;
import me.silvermail.backend.exception.email.external.InvalidContactAliasException;
import me.silvermail.backend.service.ContactService;
import me.silvermail.backend.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import me.silvermail.backend.entity.alias.MailboxAlias;
import me.silvermail.backend.model.email.external.ExternalEmail;
import me.silvermail.backend.service.AliasService;
import me.silvermail.backend.service.ExternalEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ExternalEmailHandlerServiceTest {
    @Autowired
    protected ExternalEmailHandlerService externalEmailHandlerService;
    @Autowired
    protected JavaMailSender externalEmailSender;
    @Autowired
    protected AliasService aliasService;

    @MockBean
    protected ExternalEmailService externalEmailService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;

    @Test
    public void testHandleInboundEmailWithHardReplaceModeAndContactsCreation() throws Exception {
        InternetAddress originalSmtpSenderAddress = new InternetAddress("external-user1@external.com");
        InternetAddress originalSmtpRecipientAddress = new InternetAddress("user1-mailboxalias1@user1-subdomain1.silvermail.me");

        MimeMessage mimeMessage = externalEmailSender.createMimeMessage();
        mimeMessage.setFrom(originalSmtpSenderAddress);
        mimeMessage.setRecipient(Message.RecipientType.TO, originalSmtpRecipientAddress);

        ExternalEmail externalEmail = new ExternalEmail(
                new InternetAddress(originalSmtpSenderAddress.getAddress()),
                new InternetAddress(originalSmtpRecipientAddress.getAddress()),
                mimeMessage
        );

        externalEmailHandlerService.handleEmail(externalEmail);

        // check if we sent the email
        verify(externalEmailService, times(1)).sendEmail(externalEmail);

        MailboxAlias mailboxAlias = aliasService.getMailboxAliasByAliasValue(originalSmtpRecipientAddress.getAddress());
        List<ContactAlias> contactAliasList = mailboxAlias.getContactAliasList().stream()
                // check contact have right email
                .filter(a -> a.getContact().getEmail().equals(originalSmtpSenderAddress.getAddress()))
                .toList();

        // check we have only one contact alias for mailbox alias => contact
        assertEquals(1, contactAliasList.size());
        ContactAlias contactAlias = contactAliasList.getFirst();

        // check smtp from
        assertEquals(contactAlias.getValue(), externalEmail.getSmtpSenderAddress().getAddress());
        // check smtp to
        assertEquals(mailboxAlias.getMailbox().getEmail(), externalEmail.getSmtpRecipientAddress().getAddress());

        List<InternetAddress> mimeMessageSenderAddressList = Arrays.stream(externalEmail.getMimeMessage().getFrom())
                .map(address -> (InternetAddress) address)
                .toList();
        // check we have only 1 from address
        assertEquals(1, mimeMessageSenderAddressList.size());
        InternetAddress mimeMessageSenderAddress = mimeMessageSenderAddressList.getFirst();
        // check header from
        assertEquals(contactAlias.getValue(), mimeMessageSenderAddress.getAddress());

        List<InternetAddress> mimeMessageRecipientAddressList = Arrays.stream(externalEmail.getMimeMessage().getAllRecipients())
                .map(address -> (InternetAddress) address)
                .toList();
        // check we have only 1 recipient
        assertEquals(1, mimeMessageRecipientAddressList.size());
        InternetAddress mimeMessageRecipientAddress = mimeMessageRecipientAddressList.getFirst();
        // check header to
        assertEquals(originalSmtpRecipientAddress.getAddress(), mimeMessageRecipientAddress.getAddress());

        List<InternetAddress> mimeMessageReplyToAddressList = Arrays.stream(externalEmail.getMimeMessage().getReplyTo())
                .map(address -> (InternetAddress) address)
                .toList();
        // check we have only 1 reply to address
        assertEquals(1, mimeMessageReplyToAddressList.size());
        InternetAddress mimeMessageReplyToAddress = mimeMessageReplyToAddressList.getFirst();
        // check header reply to
        assertEquals(contactAlias.getValue(), mimeMessageReplyToAddress.getAddress());
    }

    @Test
    public void testHandleOutboundEmailWithHardReplaceMode() throws Exception {
        String originalSmtpSender = "user1-mailbox1@external.com";
        String originalSmtpRecipient = "user1-contactalias1@user1-subdomain1.silvermail.me";

        MimeMessage mimeMessage = externalEmailSender.createMimeMessage();
        mimeMessage.setFrom(originalSmtpSender);
        // owner user mailbox alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress("user1-mailboxalias1@user1-subdomain1.silvermail.me")
        );
        // another user mailbox alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress("user2-mailboxalias1@user2-subdomain1.silvermail.me")
        );
        // owner contact alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(originalSmtpRecipient)
        );
        // another user contact alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress("user2-contactalias1@user2-subdomain1.silvermail.me")
        );
        // external unknown address
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress("external-user@external.com")
        );

        ExternalEmail externalEmail = new ExternalEmail(
                new InternetAddress(originalSmtpSender),
                new InternetAddress(originalSmtpRecipient),
                mimeMessage
        );

        externalEmailHandlerService.handleEmail(externalEmail);
        verify(externalEmailService, times(1)).sendEmail(externalEmail);

        validateOutboundExternalEmail(
                externalEmail,
                originalSmtpSender,
                originalSmtpRecipient
        );
    }

    @Test
    public void testHandleInboundEmailWithHardReplaceMode() throws Exception {
        String originalSmtpSender = "external-user1@external.com";
        String originalSmtpRecipient = "user1-mailboxalias1@user1-subdomain1.silvermail.me";

        MimeMessage mimeMessage = externalEmailSender.createMimeMessage();
        mimeMessage.setFrom(originalSmtpSender);
        // owner mailbox alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(originalSmtpRecipient)
        );
        // another user mailbox alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress("user2-mailboxalias1@user2-subdomain1.silvermail.me")
        );
        // owner contact alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress("user1-contactalias1@user1-subdomain1.silvermail.me")
        );
        // another user contact alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress("user2-contactalias1@user2-subdomain1.silvermail.me")
        );
        // external unknown address
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress("external-user@external.com")
        );

        ExternalEmail externalEmail = new ExternalEmail(
                new InternetAddress(originalSmtpSender),
                new InternetAddress(originalSmtpRecipient),
                mimeMessage
        );

        externalEmailHandlerService.handleEmail(externalEmail);
        verify(externalEmailService, times(1)).sendEmail(externalEmail);

        validateInboundExternalEmailWithHardReplace(
                externalEmail,
                originalSmtpSender,
                originalSmtpRecipient
        );
    }

    @Test
    public void testHandleInboundEmailWithSoftReplaceMode() throws Exception {
        User user = userService.getUserByEmail("user1@external.com");
        user.setEmailMode(User.EMAIL_MODE_SOFT_REPLACE);
        userService.saveUser(user);

        String originalSmtpSender = "external-user1@external.com";
        String originalSmtpRecipient = "user1-mailboxalias1@user1-subdomain1.silvermail.me";

        MimeMessage mimeMessage = externalEmailSender.createMimeMessage();
        mimeMessage.setFrom(originalSmtpSender);
        // owner mailbox alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(originalSmtpRecipient)
        );
        // another user mailbox alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress("user2-mailboxalias1@user2-subdomain1.silvermail.me")
        );
        // owner contact alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress("user1-contactalias1@user1-subdomain1.silvermail.me")
        );
        // another user contact alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress("user2-contactalias1@user2-subdomain1.silvermail.me")
        );
        // external unknown address
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress("external-user@external.com")
        );

        ExternalEmail externalEmail = new ExternalEmail(
                new InternetAddress(originalSmtpSender),
                new InternetAddress(originalSmtpRecipient),
                mimeMessage
        );

        Map<String, InternetAddress> originalHeaderRecipientList = Arrays.stream(externalEmail.getMimeMessage().getAllRecipients())
                .map(address -> (InternetAddress) address)
                .collect(Collectors.toMap(InternetAddress::getAddress, address -> address));

        externalEmailHandlerService.handleEmail(externalEmail);
        verify(externalEmailService, times(1)).sendEmail(externalEmail);

        validateInboundExternalEmailWithSoftReplace(
                externalEmail,
                originalSmtpSender,
                originalSmtpRecipient,
                originalHeaderRecipientList
        );
    }

    @Test
    public void testHandleOutboundEmailToOwnedMailboxAliasWithLoop() throws Exception {
        String originalSmtpSender = "user1-mailbox1@external.com";
        String originalSmtpRecipient = "user1-mailboxalias1@user1-subdomain1.silvermail.me";

        MimeMessage mimeMessage = externalEmailSender.createMimeMessage();
        mimeMessage.setFrom(originalSmtpSender);
        // owner user mailbox alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(originalSmtpRecipient)
        );

        ExternalEmail externalEmail = new ExternalEmail(
                new InternetAddress(originalSmtpSender),
                new InternetAddress(originalSmtpRecipient),
                mimeMessage
        );

        Exception exception = assertThrows(EmailSendLoopException.class, () -> {
            externalEmailHandlerService.handleEmail(externalEmail);
        });

        assertNotNull(exception);
        verify(externalEmailService, times(0)).sendEmail(externalEmail);
    }

    @Test
    public void testHandleOutboundEmailToOwnedContactAliasWithLoop() throws Exception {
        String originalSmtpSender = "user1-mailbox1@external.com";
        String originalSmtpRecipient = "user1-contactalias2@user1-subdomain1.silvermail.me";

        MimeMessage mimeMessage = externalEmailSender.createMimeMessage();
        mimeMessage.setFrom(originalSmtpSender);
        // owner user mailbox alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(originalSmtpRecipient)
        );

        ExternalEmail externalEmail = new ExternalEmail(
                new InternetAddress(originalSmtpSender),
                new InternetAddress(originalSmtpRecipient),
                mimeMessage
        );

        Exception exception = assertThrows(EmailSendLoopException.class, () -> {
            externalEmailHandlerService.handleEmail(externalEmail);
        });

        assertNotNull(exception);
        verify(externalEmailService, times(0)).sendEmail(externalEmail);
    }

    @Test
    public void testHandleOutboundEmailToNotOwnedContactAlias() throws Exception {
        String originalSmtpSender = "user1-mailbox1@external.com";
        String originalSmtpRecipient = "user2-contactalias1@user2-subdomain1.silvermail.me";

        MimeMessage mimeMessage = externalEmailSender.createMimeMessage();
        mimeMessage.setFrom(originalSmtpSender);
        // owner user mailbox alias
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(originalSmtpRecipient)
        );

        ExternalEmail externalEmail = new ExternalEmail(
                new InternetAddress(originalSmtpSender),
                new InternetAddress(originalSmtpRecipient),
                mimeMessage
        );

        Exception exception = assertThrows(InvalidContactAliasException.class, () -> {
            externalEmailHandlerService.handleEmail(externalEmail);
        });

        assertNotNull(exception);
        verify(externalEmailService, times(0)).sendEmail(externalEmail);
    }

    public void validateOutboundExternalEmail(
            ExternalEmail externalEmail,
            String originalSmtpSender,
            String originalSmtpRecipient
    ) throws Exception {
        ContactAlias contactAlias = aliasService.getContactAliasByAliasValue(originalSmtpRecipient);

        // check smtp from
        assertEquals(contactAlias.getMailboxAlias().getValue(), externalEmail.getSmtpSenderAddress().getAddress());
        // check smtp to
        assertEquals(contactAlias.getContact().getEmail(), externalEmail.getSmtpRecipientAddress().getAddress());

        // check header from
        assertEquals(
                contactAlias.getMailboxAlias().getValue(),
                Arrays.stream(externalEmail.getMimeMessage().getFrom())
                        .map(address -> (InternetAddress) address)
                        .toList().getFirst().getAddress()
        );

        // check header reply to
        assertEquals(
                contactAlias.getMailboxAlias().getValue(),
                Arrays.stream(externalEmail.getMimeMessage().getReplyTo())
                        .map(address -> (InternetAddress) address)
                        .toList().getFirst().getAddress()
        );

        // check header to
        List<InternetAddress> recipientList = Arrays.stream(externalEmail.getMimeMessage().getAllRecipients())
                .map(address -> (InternetAddress) address)
                .toList();

        for (InternetAddress recipient : recipientList) {
            ContactAlias alias = aliasService.getContactAliasByAliasValue(recipient.getAddress());
            if (
                    alias != null &&
                    Objects.equals(alias.getUser().getId(), contactAlias.getUser().getId())
            ) {
                assertEquals(1, 0);
            }
        }
    }

    public void validateInboundExternalEmailWithHardReplace(
            ExternalEmail externalEmail,
            String originalSmtpSender,
            String originalSmtpRecipient
    ) throws Exception {
        MailboxAlias mailboxAlias = aliasService.getMailboxAliasByAliasValue(originalSmtpRecipient);
        Contact contact = contactService.getContactByUserIdAndEmail(mailboxAlias.getUser().getId(), originalSmtpSender);
        ContactAlias contactAlias = aliasService.getContactAliasByContactIdAndMailboxAliasId(
                contact.getId(),
                mailboxAlias.getId()
        );

        // check smtp from
        assertEquals(contactAlias.getValue(), externalEmail.getSmtpSenderAddress().getAddress());
        // check smtp to
        assertEquals(mailboxAlias.getMailbox().getEmail(), externalEmail.getSmtpRecipientAddress().getAddress());

        // check header from
        assertEquals(
                contactAlias.getValue(),
                Arrays.stream(externalEmail.getMimeMessage().getFrom())
                        .map(address -> (InternetAddress) address)
                        .toList().getFirst().getAddress()
        );

        // check header reply to
        assertEquals(
                contactAlias.getValue(),
                Arrays.stream(externalEmail.getMimeMessage().getReplyTo())
                        .map(address -> (InternetAddress) address)
                        .toList().getFirst().getAddress()
        );

        // check header to
        List<InternetAddress> recipientList = Arrays.stream(externalEmail.getMimeMessage().getAllRecipients())
                .map(address -> (InternetAddress) address)
                .toList();

        for (InternetAddress recipient : recipientList) {
            AbstractAlias alias = aliasService.getAliasByValue(recipient.getAddress());
            if (alias != null) {
                if (Objects.equals(alias.getType(), ContactAlias.TYPE)) {
                    continue;
                }

                if (
                        Objects.equals(alias.getType(), MailboxAlias.TYPE) &&
                        Objects.equals(alias.getUser().getId(), mailboxAlias.getUser().getId())
                ) {
                    continue;
                }
            }

            assertEquals(1, 0); // throw exception if something else is met
        }
    }

    public void validateInboundExternalEmailWithSoftReplace(
            ExternalEmail externalEmail,
            String originalSmtpSender,
            String originalSmtpRecipient,
            Map<String, InternetAddress> originalHeaderRecipientList
    ) throws Exception {
        MailboxAlias mailboxAlias = aliasService.getMailboxAliasByAliasValue(originalSmtpRecipient);

        // check smtp from
        assertEquals(originalSmtpSender, externalEmail.getSmtpSenderAddress().getAddress());
        // check smtp to
        assertEquals(mailboxAlias.getMailbox().getEmail(), externalEmail.getSmtpRecipientAddress().getAddress());
        Contact contact = contactService.getContactByUserIdAndEmail(mailboxAlias.getUser().getId(), originalSmtpSender);
        ContactAlias contactAlias = aliasService.getContactAliasByContactIdAndMailboxAliasId(
                contact.getId(),
                mailboxAlias.getId()
        );

        // check header from
        assertEquals(
                originalSmtpSender,
                Arrays.stream(externalEmail.getMimeMessage().getFrom())
                        .map(address -> (InternetAddress) address)
                        .toList().getFirst().getAddress()
        );

        // check header reply to
        assertEquals(
                contactAlias.getValue(),
                Arrays.stream(externalEmail.getMimeMessage().getReplyTo())
                        .map(address -> (InternetAddress) address)
                        .toList().getFirst().getAddress()
        );

        // check header to
        List<InternetAddress> recipientList = Arrays.stream(externalEmail.getMimeMessage().getAllRecipients())
                .map(address -> (InternetAddress) address)
                .toList();

        for (InternetAddress recipient : recipientList) {
            if (originalHeaderRecipientList.get(recipient.getAddress()) == null) {
                assertEquals(1, 0);
            }
        }
    }
}
