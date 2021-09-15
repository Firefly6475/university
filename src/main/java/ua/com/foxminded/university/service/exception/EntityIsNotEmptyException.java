package ua.com.foxminded.university.service.exception;

public class EntityIsNotEmptyException extends RuntimeException {
    public EntityIsNotEmptyException(String message) {
        super(message);
    }
}
