package ru.rubik.ewmservice.user.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserEmailUniqException extends RuntimeException {
    private UUID id;

    public UserEmailUniqException() {
        id = UUID.randomUUID();
    }

    public UserEmailUniqException(String message) {
        super(message);
    }
}
