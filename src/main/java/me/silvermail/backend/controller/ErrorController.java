package me.silvermail.backend.controller;

import me.silvermail.backend.dto.controller.response.error.ErrorResponseDto;
import me.silvermail.backend.exception.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
@RestController
public class ErrorController {
    @ExceptionHandler(value = { BadRequestHttpException.class })
    public ResponseEntity<ErrorResponseDto> badRequestException(BadRequestHttpException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setError(e.getMessage());

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { AccessDeniedHttpException.class })
    public ResponseEntity<ErrorResponseDto> accessDeniedException(AccessDeniedHttpException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setError(e.getMessage());

        return new ResponseEntity<>(errorResponseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = { NotFoundHttpException.class })
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundHttpException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setError(e.getMessage());

        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { UnauthorizedHttpException.class })
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedException(UnauthorizedHttpException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setError(e.getMessage());

        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = { TooManyRequestsHttpException.class })
    public ResponseEntity<ErrorResponseDto> handleTooManyRequestsException(TooManyRequestsHttpException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setError(e.getMessage());

        return new ResponseEntity<>(errorResponseDto, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingPathVariableException(MissingPathVariableException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setError(e.getMessage());

        ResponseEntity<ErrorResponseDto> result = new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);

        if (!e.getParameter().getNestedParameterType().isPrimitive()) {
            result = new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
        }

        return result;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errorResponseDto.setError((errorResponseDto.getError() != null && !errorResponseDto.getError().isEmpty() ? errorResponseDto.getError() + "; " : "") + fieldName + ": " + errorMessage);
        });
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }
}