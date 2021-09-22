package ua.com.foxminded.university.service.validator;

import org.junit.jupiter.api.Test;
import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.service.exception.InvalidNameException;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FacultyValidatorTest {
    private final Validator<Faculty> validator = new FacultyValidator();

    @Test
    void validateShouldNotThrowException() {
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .withGroups(new ArrayList<>())
                .build();

        assertDoesNotThrow(() -> validator.validate(faculty));
    }

    @Test
    void validateShouldThrowInvalidNameExceptionIfNameIsLessThan4Symbols() {
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("hi")
                .withGroups(new ArrayList<>())
                .build();

        Exception exception = assertThrows(InvalidNameException.class, () -> validator.validate(faculty));

        String expectedMessage = "Faculty name is less than 4 or more than 30 symbols";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
