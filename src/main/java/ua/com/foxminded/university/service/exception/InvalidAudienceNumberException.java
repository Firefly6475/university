package ua.com.foxminded.university.service.exception;

public class InvalidAudienceNumberException extends RuntimeException {
    public InvalidAudienceNumberException(String message) {
        super(message);
    }
}
