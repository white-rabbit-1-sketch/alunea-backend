package me.silvermail.backend.entity;

import jakarta.persistence.*;
import me.silvermail.backend.entity.alias.ContactAlias;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    protected String id;

    @ManyToOne
    protected User user;

    protected String email;

    protected boolean isEnabled = true;

    protected Date createTime;

    @OneToMany(mappedBy = "contact", fetch = FetchType.EAGER)
    protected List<ContactAlias> contactAliasList = new ArrayList<>();

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

    public List<ContactAlias> getContactAliasList() {
        return contactAliasList;
    }

    public void setContactAliasList(List<ContactAlias> contactAliasList) {
        this.contactAliasList = contactAliasList;
    }

    public String toString() {
        return "Contact[id=" + this.id + ", email=" + this.email + "]";
    }
}