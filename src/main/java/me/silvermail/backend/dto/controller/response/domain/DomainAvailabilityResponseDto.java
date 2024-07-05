package me.silvermail.backend.dto.controller.response.domain;

import me.silvermail.backend.dto.controller.response.BaseResponseDto;

public class DomainAvailabilityResponseDto extends BaseResponseDto {
    protected boolean isAvailable = false;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
