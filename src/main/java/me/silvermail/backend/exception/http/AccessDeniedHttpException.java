package me.silvermail.backend.exception.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedHttpException extends RuntimeException {
    public AccessDeniedHttpException(String message) {
        super(message);
    }
}