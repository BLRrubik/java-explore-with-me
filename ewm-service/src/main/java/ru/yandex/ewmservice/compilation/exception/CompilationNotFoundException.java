package ru.yandex.ewmservice.compilation.exception;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException() {
    }

    public CompilationNotFoundException(String message) {
        super(message);
    }
}
