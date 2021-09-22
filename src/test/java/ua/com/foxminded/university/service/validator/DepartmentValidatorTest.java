package ua.com.foxminded.university.service.validator;

import org.junit.jupiter.api.Test;
import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.service.exception.InvalidNameException;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DepartmentValidatorTest {
    private final Validator<Department> validator = new DepartmentValidator();

    @Test
    void validateShouldNotThrowException() {
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Java")
                .withTeachers(new ArrayList<>())
                .build();

        assertDoesNotThrow(() -> validator.validate(department));
    }

    @Test
    void validateShouldThrowInvalidNameExceptionIfNameIsLessThan4Symbols() {
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("hi")
                .withTeachers(new ArrayList<>())
                .build();

        Exception exception = assertThrows(InvalidNameException.class, () -> validator.validate(department));

        String expectedMessage = "Department name is less than 4 or more than 30 symbols";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
