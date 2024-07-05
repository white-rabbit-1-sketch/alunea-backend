package me.silvermail.backend.dto.controller.response;

public class BaseResponseDto {
    boolean result = true;

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
