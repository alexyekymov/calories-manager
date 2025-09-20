package dev.overlax.caloriesmanager.util.exception;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(message, ErrorType.NOT_FOUND);
    }
}
