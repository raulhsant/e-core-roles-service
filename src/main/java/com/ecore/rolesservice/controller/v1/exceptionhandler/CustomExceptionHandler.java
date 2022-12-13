package com.ecore.rolesservice.controller.v1.exceptionhandler;

import com.ecore.rolesservice.exception.HttpException;
import com.ecore.rolesservice.model.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    private static List<String> getErrors(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return errors;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        String message = String.format("Found %s errors on request", ex.getErrorCount());

        List<String> errors = getErrors(ex);

        logException(ex);
        final var errorDto = ErrorDto.builder().errors(errors).message(message).build();
        return handleExceptionInternal(ex, errorDto, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({HttpException.class})
    public ResponseEntity<Object> handleExpectedHttpExecptions(HttpException ex) {
        final var errorDto = ErrorDto.builder()
                .message(ex.getLocalizedMessage())
                .errors(Collections.singletonList(ex.getError()))
                .build();
        logException(ex);
        return new ResponseEntity<>(errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        final var errors = Collections.singletonList("An unexpected Error occurred");
        final var errorDto = ErrorDto.builder()
                .message(ex.getLocalizedMessage())
                .errors(errors)
                .build();
        logException(ex);
        return new ResponseEntity<>(errorDto, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void logException(Exception ex) {
        log.error("Error caught: " + ex.getLocalizedMessage(), ex);
    }
}
