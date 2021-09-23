package ua.com.foxminded.university.service.validator;

import org.junit.jupiter.api.Test;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.service.exception.InvalidLessonDateException;
import ua.com.foxminded.university.service.exception.InvalidLessonDurationException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LessonValidatorTest {
    private final Validator<Lesson> validator = new LessonValidator();

    @Test
    void validateShouldNotThrowException() {
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDate(LocalDate.parse("2021-10-10"))
                .withTimeStart(LocalTime.parse("11:30:00"))
                .withTimeEnd(LocalTime.parse("13:00:00"))
                .build();

        assertDoesNotThrow(() -> validator.validate(lesson));
    }

    @Test
    void validateShouldThrowInvalidLessonDurationExceptionIfTimeStartDiffersWithTimeEndMoreThan90Minutes() {
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDate(LocalDate.parse("2021-10-10"))
                .withTimeStart(LocalTime.parse("11:30:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        Exception exception = assertThrows(InvalidLessonDurationException.class, () -> validator.validate(lesson));

        String expectedMessage = "Lesson duration is more or less than 90 minutes";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowInvalidLessonDateExceptionIfLessonDateIsBeforeCurrentDate() {
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDate(LocalDate.parse("2020-10-10"))
                .withTimeStart(LocalTime.parse("11:30:00"))
                .withTimeEnd(LocalTime.parse("13:00:00"))
                .build();

        Exception exception = assertThrows(InvalidLessonDateException.class, () -> validator.validate(lesson));

        String expectedMessage = "Lesson date is before current date";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
