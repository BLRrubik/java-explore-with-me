package ru.yandex.ewmservice.event.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException() {
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}
