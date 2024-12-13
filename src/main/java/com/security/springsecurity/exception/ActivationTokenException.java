package com.security.springsecurity.exception;

public class ActivationTokenException extends RuntimeException {
    public ActivationTokenException(String message) {
        super(message);
    }
}