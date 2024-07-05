package me.silvermail.backend.exception.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class TooManyRequestsHttpException extends RuntimeException {
    public TooManyRequestsHttpException(String message) {
        super(message);
    }
}