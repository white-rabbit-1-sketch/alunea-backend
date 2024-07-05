package me.silvermail.backend.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import me.silvermail.backend.model.email.internal.AbstractInternalEmail;

public class InternalEmailMessage {
    protected AbstractInternalEmail email;

    @JsonCreator
    public InternalEmailMessage(@JsonProperty("email") AbstractInternalEmail email) {
        this.email = email;
    }

    public AbstractInternalEmail getEmail() {
        return email;
    }

    public void setEmail(AbstractInternalEmail email) {
        this.email = email;
    }
}
