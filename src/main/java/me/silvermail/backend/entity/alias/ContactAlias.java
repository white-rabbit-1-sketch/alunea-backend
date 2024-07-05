package me.silvermail.backend.entity.alias;

import jakarta.persistence.*;
import me.silvermail.backend.entity.Contact;

@Entity
@DiscriminatorValue("contact")
public class ContactAlias extends AbstractAlias {
    public static final String TYPE = "contact";

    @ManyToOne
    protected Contact contact;

    @ManyToOne
    protected MailboxAlias mailboxAlias;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public MailboxAlias getMailboxAlias() {
        return mailboxAlias;
    }

    public void setMailboxAlias(MailboxAlias mailboxAlias) {
        this.mailboxAlias = mailboxAlias;
    }

    @Override
    public String toString() {
        return "ContactAlias[" + super.toString() + ", contact=" + contact + ", mailboxAlias=" + this.mailboxAlias + "]";
    }
}