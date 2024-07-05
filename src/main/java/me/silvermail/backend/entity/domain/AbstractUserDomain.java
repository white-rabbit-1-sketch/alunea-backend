package me.silvermail.backend.entity.domain;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import me.silvermail.backend.entity.Mailbox;
import me.silvermail.backend.entity.User;

@MappedSuperclass
public abstract class AbstractUserDomain extends AbstractDomain {
    @ManyToOne
    protected User user;

    @ManyToOne
    protected Mailbox catchAllMailbox;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Mailbox getCatchAllMailbox() {
        return catchAllMailbox;
    }

    public void setCatchAllMailbox(Mailbox catchAllMailbox) {
        this.catchAllMailbox = catchAllMailbox;
    }
}