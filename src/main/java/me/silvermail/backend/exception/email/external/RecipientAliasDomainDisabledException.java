package me.silvermail.backend.exception.email.external;

public class RecipientAliasDomainDisabledException extends AbstractEmailRejectedException {
    public RecipientAliasDomainDisabledException() {
        super("Requested action not taken: recipient alias domain is disabled");
    }

    @Override
    public int getCode() {
        return 550;
    }
}