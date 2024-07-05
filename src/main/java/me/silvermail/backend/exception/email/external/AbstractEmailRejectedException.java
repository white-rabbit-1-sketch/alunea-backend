package me.silvermail.backend.exception.email.external;

abstract public class AbstractEmailRejectedException extends RuntimeException {
    public AbstractEmailRejectedException(String message) {
        super(message);
    }

    public AbstractEmailRejectedException(String message, Throwable cause) {
        super(message, cause);
    }

    abstract public int getCode();
}
