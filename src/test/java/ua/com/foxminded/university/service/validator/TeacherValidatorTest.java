package ua.com.foxminded.university.service.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.exception.InvalidBirthdayException;
import ua.com.foxminded.university.service.exception.InvalidEmailException;
import ua.com.foxminded.university.service.exception.InvalidNameException;
import ua.com.foxminded.university.service.exception.InvalidPasswordException;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TeacherValidatorTest {
    private final Validator<Teacher> validator = new TeacherValidator();

    @Test
    void validateShouldNotThrowException() {
        Teacher teacher = Teacher.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("hello@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("hello")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();
        Assertions.assertDoesNotThrow(() -> validator.validate(teacher));
    }

    @Test
    void validateShouldThrowInvalidNameExceptionIfNameIsLessThan2Symbols() {
        Teacher teacher = Teacher.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("hello@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("h")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        Exception exception = assertThrows(InvalidNameException.class, () -> validator.validate(teacher));

        String expectedMessage = "Name is less than 2 symbols or contains numbers / non-word symbols";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowInvalidBirthdayExceptionIfAgeIsLessThan20() {
        Teacher teacher = Teacher.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("hello@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("hello")
                .withBirthday(LocalDate.parse("2010-01-01"))
                .build();

        Exception exception = assertThrows(InvalidBirthdayException.class, () -> validator.validate(teacher));

        String expectedMessage = "Teacher age is lower than minimum of 20";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowInvalidEmailExceptionIfEmailInvalid() {
        Teacher teacher = Teacher.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("hi")
                .withPassword("mYP@sSw0rd")
                .withName("hello")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        Exception exception = assertThrows(InvalidEmailException.class, () -> validator.validate(teacher));

        String expectedMessage = "Email invalid";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowInvalidPasswordExceptionIfPassword5Symbols() {
        Teacher teacher = Teacher.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("hi@gmail.com")
                .withPassword("H3!lo")
                .withName("hello")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        Exception exception = assertThrows(InvalidPasswordException.class, () -> validator.validate(teacher));

        String expectedMessage = "Password must have at least 8 symbols, 1 upper-case symbol, 1 digit and 1 special character";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
