package me.silvermail.backend.model.email.internal;

public abstract class AbstractUserEmail extends AbstractInternalEmail {
    protected String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
