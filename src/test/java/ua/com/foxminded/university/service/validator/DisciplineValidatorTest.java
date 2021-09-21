package ua.com.foxminded.university.service.validator;

import org.junit.jupiter.api.Test;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.service.exception.InvalidNameException;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DisciplineValidatorTest {
    private final Validator<Discipline> validator = new DisciplineValidator();

    @Test
    void validateShouldNotThrowException() {
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Java")
                .withTeachers(new ArrayList<>())
                .build();

        assertDoesNotThrow(() -> validator.validate(discipline));
    }

    @Test
    void validateShouldThrowInvalidNameExceptionIfNameIsLessThan4Symbols() {
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("hi")
                .withTeachers(new ArrayList<>())
                .build();

        Exception exception = assertThrows(InvalidNameException.class, () -> validator.validate(discipline));

        String expectedMessage = "Discipline name is less than 4 or more than 30 symbols";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
