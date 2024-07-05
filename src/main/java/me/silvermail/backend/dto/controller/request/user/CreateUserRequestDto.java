package me.silvermail.backend.dto.controller.request.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateUserRequestDto {
    @Pattern(
            regexp = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,6}$",
            message = "Invalid email format"
    )
    protected String email;
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    protected String password;
    protected String language;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
