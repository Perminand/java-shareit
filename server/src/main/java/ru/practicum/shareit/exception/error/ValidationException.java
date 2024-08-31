package ru.practicum.shareit.exception.error;

public class ValidationException extends RuntimeException {
    public ValidationException(String s) {
        super(s);
    }
}
