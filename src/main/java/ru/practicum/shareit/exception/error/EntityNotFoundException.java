package ru.practicum.shareit.exception.error;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String massage) {
        super(massage);
    }
}
