package me.silvermail.backend.dto.controller.response.user;

import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.entity.User;

public class UserResponseDto extends BaseResponseDto {
    protected User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
