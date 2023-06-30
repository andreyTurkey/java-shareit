package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(AvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAvailableException(final AvailableException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFoundException(final EntityNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundExceptionCustom.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFoundException(final EntityNotFoundExceptionCustom e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleDuplicateEmailException(final DuplicateEmailException e) {
        log.error("Email exists.");
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleThereIsNoSuchUserException(MethodArgumentNotValidException ex) {
        log.error("Invalid fields");
        return new ResponseEntity<>(new ErrorResponse(ex.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }
}

