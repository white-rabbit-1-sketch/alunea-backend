package me.silvermail.backend.server.smtp.exception;

import me.silvermail.backend.exception.email.external.AbstractEmailRejectedException;

public class InvalidFromClauseException extends AbstractEmailRejectedException {
    public InvalidFromClauseException() {
        super("Requested action not taken: invalid from clause");
    }

    @Override
    public int getCode() {
        return 550;
    }
}