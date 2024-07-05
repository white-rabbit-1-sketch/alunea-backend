package me.silvermail.backend.exception.email.external;

public class MailboxDisabledException extends AbstractEmailRejectedException {
    public MailboxDisabledException() {
        super("Requested action not taken: related mailbox is disabled");
    }

    @Override
    public int getCode() {
        return 550;
    }
}