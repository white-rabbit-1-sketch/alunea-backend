package me.silvermail.backend.exception.email.external;

public class InvalidContactAliasException extends AbstractEmailRejectedException {
    public InvalidContactAliasException() {
        super("Requested action not taken: contact alias is invalid");
    }

    @Override
    public int getCode() {
        return 550;
    }
}