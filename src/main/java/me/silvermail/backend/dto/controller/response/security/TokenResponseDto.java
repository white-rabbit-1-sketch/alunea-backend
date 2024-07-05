package me.silvermail.backend.dto.controller.response.security;

import me.silvermail.backend.dto.controller.response.BaseResponseDto;

public class TokenResponseDto extends BaseResponseDto {
    protected String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
