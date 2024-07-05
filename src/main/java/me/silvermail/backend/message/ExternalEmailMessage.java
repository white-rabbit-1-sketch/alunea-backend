package me.silvermail.backend.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import me.silvermail.backend.model.email.external.ExternalEmail;

public class ExternalEmailMessage {
    protected ExternalEmail externalEmail;

    @JsonCreator
    public ExternalEmailMessage(@JsonProperty("externalEmail") ExternalEmail externalEmail) {
        this.externalEmail = externalEmail;
    }

    public ExternalEmail getExternalEmail() {
        return externalEmail;
    }

    public void setExternalEmail(ExternalEmail externalEmail) {
        this.externalEmail = externalEmail;
    }
}
