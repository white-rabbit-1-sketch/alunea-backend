package me.silvermail.backend.dto.controller.response.contact;

import me.silvermail.backend.dto.controller.response.BaseResponseDto;

public class UserContactEmailAvailabilityResponseDto extends BaseResponseDto {
    protected boolean isAvailable = false;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
