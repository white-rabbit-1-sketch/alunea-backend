package me.silvermail.backend.exception.email.external;

public class UserDisabledException extends AbstractEmailRejectedException {
    public UserDisabledException() {
        super("Requested action not taken: related user is disabled");
    }

    @Override
    public int getCode() {
        return 550;
    }
}