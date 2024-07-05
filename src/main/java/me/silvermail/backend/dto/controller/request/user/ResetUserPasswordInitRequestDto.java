package me.silvermail.backend.dto.controller.request.user;

public class ResetUserPasswordInitRequestDto {
    protected String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
