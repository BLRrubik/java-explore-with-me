package ru.rubik.ewmservice.event_request.exception;

public class RequestNotFoundException extends RuntimeException{
    public RequestNotFoundException() {
    }

    public RequestNotFoundException(String message) {
        super(message);
    }
}
