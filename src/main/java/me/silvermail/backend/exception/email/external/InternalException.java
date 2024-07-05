package me.silvermail.backend.exception.email.external;

public class InternalException extends AbstractEmailRejectedException {
    public InternalException(Throwable cause) {
        super("Transaction failed: internal error", cause);
    }

    @Override
    public int getCode() {
        return 554;
    }
}