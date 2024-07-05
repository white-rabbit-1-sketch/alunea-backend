package me.silvermail.backend.exception.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BadRequestHttpException extends RuntimeException {
    public BadRequestHttpException(String message) {
        super(message);
    }
}