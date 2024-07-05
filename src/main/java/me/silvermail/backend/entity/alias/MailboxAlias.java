package me.silvermail.backend.entity.alias;

import jakarta.persistence.*;
import me.silvermail.backend.entity.Mailbox;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("mailbox")
public class MailboxAlias extends AbstractAlias {
    public static final String TYPE = "mailbox";

    @ManyToOne
    protected Mailbox mailbox;;

    @OneToMany(mappedBy = "mailboxAlias")
    protected List<ContactAlias> contactAliasList = new ArrayList<>();

    public Mailbox getMailbox() {
        return mailbox;
    }

    public void setMailbox(Mailbox mailbox) {
        this.mailbox = mailbox;
    }

    public List<ContactAlias> getContactAliasList() {
        return contactAliasList;
    }

    public void setContactAliasList(List<ContactAlias> contactAliasList) {
        this.contactAliasList = contactAliasList;
    }

    @Override
    public String toString() {
        return "MailboxAlias[" + super.toString() + ", mailbox=" + this.mailbox + "]";
    }
}