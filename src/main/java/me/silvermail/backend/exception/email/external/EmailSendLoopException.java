package me.silvermail.backend.exception.email.external;

public class EmailSendLoopException extends AbstractEmailRejectedException {
    public EmailSendLoopException() {
        super("Requested action not taken: destination is equal to the sender");
    }

    @Override
    public int getCode() {
        return 550;
    }
}