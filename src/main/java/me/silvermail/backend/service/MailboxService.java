package me.silvermail.backend.service;

import me.silvermail.backend.entity.Mailbox;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.token.MailboxEmailVerificationToken;
import me.silvermail.backend.exception.http.AccessDeniedHttpException;
import me.silvermail.backend.repository.MailboxRepository;
import me.silvermail.backend.service.token.MailboxEmailVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MailboxService {
    @Autowired
    protected MailboxRepository mailboxRepository;
    @Autowired
    MailboxEmailVerificationTokenService mailboxEmailVerificationTokenService;

    public Mailbox getMailboxById(String id) {
        return mailboxRepository.findById(id).orElse(null);
    }

    public Mailbox getMailboxByEmail(String email) {
        return mailboxRepository.findByEmail(email);
    }

    public Mailbox getMailboxByUserIdAndEmail(String userId, String email) {
        return mailboxRepository.findByUserIdAndEmail(userId, email);
    }

    public List<Mailbox> getMailboxListByUserId(String userId) {
        return mailboxRepository.findAllByUserId(userId);
    }

    public Mailbox createMailbox(User user, String email) {
        email = email.trim();

        Mailbox exitedMailbox = getMailboxByUserIdAndEmail(user.getId(), email);
        if (exitedMailbox != null) {
            throw new IllegalArgumentException("Mailbox already exists");
        }

        Mailbox mailbox = new Mailbox();
        mailbox.setUser(user);
        mailbox.setEmail(email);
        mailbox.setCreateTime(new Date());

        mailboxRepository.save(mailbox);

        return mailbox;
    }

    @Transactional
    public void verifyMailboxEmailByToken(MailboxEmailVerificationToken token) throws Exception {
        if (token.getMailbox().isEmailVerified()) {
            throw new IllegalArgumentException("Email is already verified");
        }

        token.getMailbox().setEmailVerified(true);
        saveMailbox(token.getMailbox());

        mailboxEmailVerificationTokenService.removeToken(token);
    }

    public void saveMailbox(Mailbox mailbox) {
        mailboxRepository.save(mailbox);
    }

    public void removeMailbox(Mailbox mailbox) {
        mailboxRepository.delete(mailbox);
    }
}
