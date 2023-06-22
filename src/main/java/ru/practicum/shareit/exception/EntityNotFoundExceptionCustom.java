package ru.practicum.shareit.exception;

public class EntityNotFoundExceptionCustom extends RuntimeException {

    public EntityNotFoundExceptionCustom(String message) {

        super(message);
    }
}
