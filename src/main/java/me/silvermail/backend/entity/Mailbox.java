package me.silvermail.backend.entity;

import jakarta.persistence.*;
import me.silvermail.backend.entity.alias.MailboxAlias;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Mailbox  {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    protected String id;

    @ManyToOne
    protected User user;

    protected String email;

    protected boolean isEmailVerified;

    @OneToMany(mappedBy = "mailbox")
    protected List<MailboxAlias> mailboxAliasList = new ArrayList<>();

    protected boolean isEnabled = true;

    protected Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public List<MailboxAlias> getMailboxAliasList() {
        return mailboxAliasList;
    }

    public void setMailboxAliasList(List<MailboxAlias> mailboxAliasList) {
        this.mailboxAliasList = mailboxAliasList;
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

    public String toString() {
        return "Mailbox[id=" + this.id + ", email=" + this.email + "]";
    }
}