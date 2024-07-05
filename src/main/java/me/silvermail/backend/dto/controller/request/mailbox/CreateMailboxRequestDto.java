package me.silvermail.backend.dto.controller.request.mailbox;

import jakarta.validation.constraints.Pattern;

public class CreateMailboxRequestDto {
    @Pattern(
            regexp = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,6}$",
            message = "Invalid email format"
    )
    protected String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
