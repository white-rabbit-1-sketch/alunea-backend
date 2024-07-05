package me.silvermail.backend.dto.controller.request.user;

public class VerifyUserEmailRequestDto {
    protected String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
