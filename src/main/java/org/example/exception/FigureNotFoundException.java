package org.example.exception;

public class FigureNotFoundException extends RuntimeException {
    public FigureNotFoundException(String message) {
        super(message);
    }

    public FigureNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
