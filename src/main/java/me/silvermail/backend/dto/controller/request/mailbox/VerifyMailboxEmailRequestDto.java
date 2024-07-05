package me.silvermail.backend.dto.controller.request.mailbox;

public class VerifyMailboxEmailRequestDto {
    protected String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
