package dev.overlax.caloriesmanager.util.exception;

public class DataConflictException extends AppException {
    public DataConflictException(String message) {
        super(message, ErrorType.DATA_CONFLICT);
    }
}
