package me.silvermail.backend.dto.controller.request.user;

import jakarta.validation.constraints.Size;

public class ResetUserPasswordRequestDto {
    protected String token;
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    protected String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
