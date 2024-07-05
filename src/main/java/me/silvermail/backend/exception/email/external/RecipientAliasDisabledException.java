package me.silvermail.backend.exception.email.external;

public class RecipientAliasDisabledException extends AbstractEmailRejectedException {
    public RecipientAliasDisabledException() {
        super("Requested action not taken: recipient alias is disabled");
    }

    @Override
    public int getCode() {
        return 550;
    }
}