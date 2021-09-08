package ua.com.foxminded.university.service.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.exception.InvalidBirthdayException;
import ua.com.foxminded.university.service.exception.InvalidEmailException;
import ua.com.foxminded.university.service.exception.InvalidNameException;
import ua.com.foxminded.university.service.exception.InvalidPasswordException;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudentValidatorTest {
    private final Validator<Student> validator = new StudentValidator();

    @Test
    void validateShouldNotThrowException() {
        Student student = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("2002-01-01"))
                .build();
        Assertions.assertDoesNotThrow(() -> validator.validate(student));
    }

    @Test
    void validateShouldThrowInvalidNameExceptionIfNameIsLessThan2Symbols() {
        Student student = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("A")
                .withBirthday(LocalDate.parse("2002-01-01"))
                .build();

        Exception exception = assertThrows(InvalidNameException.class, () -> validator.validate(student));

        String expectedMessage = "Name is less than 2 symbols or contains numbers / non-word symbols";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowInvalidBirthdayExceptionIfAgeIsLessThan16() {
        Student student = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("2010-01-01"))
                .build();

        Exception exception = assertThrows(InvalidBirthdayException.class, () -> validator.validate(student));

        String expectedMessage = "Student age is lower than minimum of 16";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowInvalidEmailExceptionIfEmailInvalid() {
        Student student = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("world")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("2002-01-01"))
                .build();

        Exception exception = assertThrows(InvalidEmailException.class, () -> validator.validate(student));

        String expectedMessage = "Email invalid";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowInvalidPasswordExceptionIfPassword5Symbols() {
        Student student = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("world@gmail.com")
                .withPassword("P@ss3")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("2002-01-01"))
                .build();

        Exception exception = assertThrows(InvalidPasswordException.class, () -> validator.validate(student));

        String expectedMessage = "Password must have at least 8 symbols, 1 upper-case symbol, 1 digit and 1 special character";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
