package com.example.ncontainr.exception;

public class ContainerException extends RuntimeException {

    public ContainerException(final String message) {
        super(message);
    }

    public ContainerException(final Exception message) {
        super(message);
    }

    public ContainerException(final String message, final Exception ex) {
        super(message, ex);
    }
}

