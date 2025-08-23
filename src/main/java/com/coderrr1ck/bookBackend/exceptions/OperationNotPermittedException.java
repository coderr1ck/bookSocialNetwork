package com.coderrr1ck.bookBackend.exceptions;


public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException(String message) {
        super(message);
    }
}
