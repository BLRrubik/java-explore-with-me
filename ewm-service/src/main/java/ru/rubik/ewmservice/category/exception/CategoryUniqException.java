package ru.rubik.ewmservice.category.exception;

public class CategoryUniqException extends RuntimeException{
    public CategoryUniqException() {
    }

    public CategoryUniqException(String message) {
        super(message);
    }
}
