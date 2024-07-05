package me.silvermail.backend.entity.alias;

import jakarta.persistence.*;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.domain.AbstractDomain;
import me.silvermail.backend.entity.domain.AbstractUserDomain;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Date;

@Entity(name = "alias")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public class AbstractAlias {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    protected String id;

    @Column(insertable = false, updatable = false)
    protected String type;

    @ManyToOne
    protected User user;

    @ManyToOne
    protected AbstractDomain domain;

    protected String recipient;

    protected String value;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AbstractUserDomain getDomain() {
        return (AbstractUserDomain) domain;
    }

    public void setDomain(AbstractUserDomain domain) {
        this.domain = domain;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        return "AbstractAlias[id=" + id + ", type=" + type + ", value=" + recipient + "@" + domain.getDomain() + "]";
    }
}