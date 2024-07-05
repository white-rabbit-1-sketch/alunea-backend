package me.silvermail.backend.entity.token;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import me.silvermail.backend.entity.Mailbox;

@Entity
@DiscriminatorValue("mailbox-email-verification")
public class MailboxEmailVerificationToken extends AbstractToken {

    @ManyToOne
    protected Mailbox mailbox;

    public Mailbox getMailbox() {
        return mailbox;
    }

    public void setMailbox(Mailbox mailbox) {
        this.mailbox = mailbox;
    }
}