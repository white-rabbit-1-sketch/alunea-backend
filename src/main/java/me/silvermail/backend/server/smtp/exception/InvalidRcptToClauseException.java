package me.silvermail.backend.server.smtp.exception;

import me.silvermail.backend.exception.email.external.AbstractEmailRejectedException;

public class InvalidRcptToClauseException extends AbstractEmailRejectedException {
    public InvalidRcptToClauseException() {
        super("Requested action not taken: invalid rcpt to clause");
    }

    @Override
    public int getCode() {
        return 550;
    }
}