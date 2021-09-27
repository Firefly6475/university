package ua.com.foxminded.university.service.exception;

public class InvalidLessonDateException extends RuntimeException {
    public InvalidLessonDateException(String message) {
        super(message);
    }
}
