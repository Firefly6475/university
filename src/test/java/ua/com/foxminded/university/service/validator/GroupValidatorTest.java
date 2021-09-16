package ua.com.foxminded.university.service.validator;

import org.junit.jupiter.api.Test;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.exception.InvalidCourseException;
import ua.com.foxminded.university.service.exception.InvalidNameException;

import java.rmi.server.ExportException;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GroupValidatorTest {
    private final Validator<Group> validator = new GroupValidator();

    @Test
    void validateShouldNotThrowException() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("BI-16")
                .withCourse(4)
                .build();

        assertDoesNotThrow(() -> validator.validate(group));
    }

    @Test
    void validateShouldThrowInvalidNameExceptionIfNameFormatIsInvalid() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("hello")
                .withCourse(4)
                .build();

        Exception exception = assertThrows(InvalidNameException.class, () -> validator.validate(group));

        String expectedMessage = "Group name format invalid";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowInvalidCourseExceptionIfCourseIsAboveMaximum() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("BI-16")
                .withCourse(6)
                .build();

        Exception exception = assertThrows(InvalidCourseException.class, () -> validator.validate(group));

        String expectedMessage = "Group course is above maximum, zero or negative";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowInvalidCourseExceptionIfCourseIsZero() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("BI-16")
                .withCourse(0)
                .build();

        Exception exception = assertThrows(InvalidCourseException.class, () -> validator.validate(group));

        String expectedMessage = "Group course is above maximum, zero or negative";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
