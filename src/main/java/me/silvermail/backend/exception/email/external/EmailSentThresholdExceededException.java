package me.silvermail.backend.exception.email.external;

public class EmailSentThresholdExceededException extends AbstractEmailRejectedException {
    public EmailSentThresholdExceededException() {
        super("Requested action not taken: email sent threshold exceeded");
    }

    @Override
    public int getCode() {
        return 550;
    }
}