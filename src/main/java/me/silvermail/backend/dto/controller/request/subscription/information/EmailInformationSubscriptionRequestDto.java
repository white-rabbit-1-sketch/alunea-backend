package me.silvermail.backend.dto.controller.request.subscription.information;

import jakarta.validation.constraints.Pattern;

public class EmailInformationSubscriptionRequestDto {
    @Pattern(
            regexp = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,6}$",
            message = "Invalid email format"
    )
    protected String email;
    protected String language;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
