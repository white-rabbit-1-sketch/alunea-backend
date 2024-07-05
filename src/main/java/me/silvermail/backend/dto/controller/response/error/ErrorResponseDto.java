package me.silvermail.backend.dto.controller.response.error;

import me.silvermail.backend.dto.controller.response.BaseResponseDto;

public class ErrorResponseDto extends BaseResponseDto {
    protected String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
