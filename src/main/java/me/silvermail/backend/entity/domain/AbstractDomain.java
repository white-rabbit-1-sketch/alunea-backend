package me.silvermail.backend.entity.domain;

import jakarta.persistence.*;
import me.silvermail.backend.entity.alias.ContactAlias;
import me.silvermail.backend.entity.alias.MailboxAlias;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "domain")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public abstract class AbstractDomain {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    protected String id;

    @Column(insertable = false, updatable = false)
    protected String type;

    protected String domain;

    @OneToMany(mappedBy = "domain")
    protected List<MailboxAlias> mailboxAliasList = new ArrayList<>();

    @OneToMany(mappedBy = "domain")
    protected List<ContactAlias> contactAliasList = new ArrayList<>();

    protected boolean isEnabled = true;

    protected Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<MailboxAlias> getMailboxAliasList() {
        return mailboxAliasList;
    }

    public void setMailboxAliasList(List<MailboxAlias> mailboxAliasList) {
        this.mailboxAliasList = mailboxAliasList;
    }

    public List<ContactAlias> getContactAliasList() {
        return contactAliasList;
    }

    public void setContactAliasList(List<ContactAlias> contactAliasList) {
        this.contactAliasList = contactAliasList;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}