package me.silvermail.backend.exception.email.external;

public class InvalidEmailStructureException extends AbstractEmailRejectedException {
    public InvalidEmailStructureException() {
        super("Transaction failed: message content or structure error");
    }

    @Override
    public int getCode() {
        return 554;
    }
}