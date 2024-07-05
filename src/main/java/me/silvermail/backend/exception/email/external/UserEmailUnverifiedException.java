package me.silvermail.backend.exception.email.external;

public class UserEmailUnverifiedException extends AbstractEmailRejectedException {
    public UserEmailUnverifiedException() {
        super("Requested action not taken: related user email is not verified");
    }

    @Override
    public int getCode() {
        return 550;
    }
}