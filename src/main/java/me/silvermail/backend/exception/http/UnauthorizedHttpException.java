package me.silvermail.backend.exception.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedHttpException extends RuntimeException {
    public UnauthorizedHttpException(String message) {
        super(message);
    }
}