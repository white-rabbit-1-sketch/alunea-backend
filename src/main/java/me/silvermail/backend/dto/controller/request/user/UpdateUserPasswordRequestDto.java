package me.silvermail.backend.dto.controller.request.user;

import jakarta.validation.constraints.Size;

public class UpdateUserPasswordRequestDto {
    protected String currentPassword;
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    protected String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
