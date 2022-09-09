package ru.rubik.ewmservice.event.exception;

public class EventStateException extends RuntimeException{
    public EventStateException() {
    }

    public EventStateException(String message) {
        super(message);
    }
}
