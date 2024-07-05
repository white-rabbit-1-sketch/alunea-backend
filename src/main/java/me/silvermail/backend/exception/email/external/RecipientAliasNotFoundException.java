package me.silvermail.backend.exception.email.external;

public class RecipientAliasNotFoundException extends AbstractEmailRejectedException {
    public RecipientAliasNotFoundException() {
        super("Requested action not taken: recipient not found");
    }

    @Override
    public int getCode() {
        return 550;
    }
}