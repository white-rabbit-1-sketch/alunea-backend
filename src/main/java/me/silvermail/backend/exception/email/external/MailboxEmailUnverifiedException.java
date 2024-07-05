package me.silvermail.backend.exception.email.external;

public class MailboxEmailUnverifiedException extends AbstractEmailRejectedException {
    public MailboxEmailUnverifiedException() {
        super("Requested action not taken: related mailbox email is not verified");
    }

    @Override
    public int getCode() {
        return 550;
    }
}