package ru.rubik.ewmservice.compilation.exception;

public class CompilationUniqException extends RuntimeException {
    public CompilationUniqException() {
    }

    public CompilationUniqException(String message) {
        super(message);
    }
}
