package ru.yandex.ewmservice.event_request.exception;

public class RequestIllegalStateException extends RuntimeException {
    public RequestIllegalStateException() {
    }

    public RequestIllegalStateException(String message) {
        super(message);
    }
}
