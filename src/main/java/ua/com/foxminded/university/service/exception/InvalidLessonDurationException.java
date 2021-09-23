package ua.com.foxminded.university.service.exception;

public class InvalidLessonDurationException extends RuntimeException {
    public InvalidLessonDurationException(String message) {
        super(message);
    }
}
