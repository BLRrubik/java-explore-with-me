package ru.rubik.ewmservice.user.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserNotFoundException extends RuntimeException {
    private UUID id;

    public UserNotFoundException() {
        id = UUID.randomUUID();
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
