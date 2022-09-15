package ru.yandex.ewmservice.category.exception;

import java.util.UUID;

public class CategoryUniqException extends RuntimeException {
    private UUID id;

    public CategoryUniqException() {
        id = UUID.randomUUID();
    }

    public CategoryUniqException(String message) {
        super(message);
    }
}
