package com.ecore.rolesservice.exception;

import lombok.Getter;

public class HttpException extends RuntimeException {

    @Getter
    protected String error;

    public HttpException(String message) {
        super(message);
    }
}
