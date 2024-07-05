package me.silvermail.backend.dto.controller.request.contact;

import jakarta.validation.constraints.Pattern;

public class CreateContactRequestDto {
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
