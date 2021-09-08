package ua.com.foxminded.university.service.validator;

import org.junit.jupiter.api.Test;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.service.exception.InvalidAudienceFloorException;
import ua.com.foxminded.university.service.exception.InvalidAudienceNumberException;
import ua.com.foxminded.university.service.exception.InvalidNumberOnFloorException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AudienceValidatorTest {
    private final Validator<Audience> validator = new AudienceValidator();

    @Test
    void validateShouldNotThrowException() {
        Audience audience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withFloor(1)
                .withNumber(149)
                .build();

        assertDoesNotThrow(() -> validator.validate(audience));
    }

    @Test
    void validateShouldThrowInvalidAudienceNumberExceptionIfNumberIsZero() {
        Audience audience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withFloor(1)
                .withNumber(0)
                .build();

        Exception exception = assertThrows(InvalidAudienceNumberException.class, () -> validator.validate(audience));

        String expectedMessage = "Number is starting with zero, negative or have less than 3 symbols";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowInvalidAudienceFloorExceptionIfFloorIsAbove5() {
        Audience audience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withFloor(6)
                .withNumber(543)
                .build();

        Exception exception = assertThrows(InvalidAudienceFloorException.class, () -> validator.validate(audience));

        String expectedMessage = "Floor is zero, negative or don't exist";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowInvalidNumberOnFloorExceptionIfNumberInvalidForSpecifiedFloor() {
        Audience audience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withFloor(3)
                .withNumber(145)
                .build();

        Exception exception = assertThrows(InvalidNumberOnFloorException.class, () -> validator.validate(audience));

        String expectedMessage = "This audience can't be on specified floor";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
