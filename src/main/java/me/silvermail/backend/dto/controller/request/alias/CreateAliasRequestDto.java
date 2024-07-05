package me.silvermail.backend.dto.controller.request.alias;

import jakarta.validation.constraints.Pattern;

public class CreateAliasRequestDto {
    @Pattern(regexp = "^(?![-_\\.])[A-Za-z0-9-_\\.]{1,64}(?<![-_\\.])$", message = "Invalid recipient format")
    protected String recipient;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
