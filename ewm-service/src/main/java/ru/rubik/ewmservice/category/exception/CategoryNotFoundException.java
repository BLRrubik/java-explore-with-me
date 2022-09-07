package ru.rubik.ewmservice.category.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryNotFoundException extends RuntimeException{
    private UUID id;

    public CategoryNotFoundException() {
        id = UUID.randomUUID();
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
