package me.silvermail.backend.exception.email.external;

public class MailboxUnavailableException extends AbstractEmailRejectedException {
    public MailboxUnavailableException() {
        super("Requested action not taken: mailbox unavailable");
    }

    @Override
    public int getCode() {
        return 550;
    }
}