package ua.com.foxminded.university.service.validator;

import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.service.exception.InvalidLessonDateException;
import ua.com.foxminded.university.service.exception.InvalidLessonDurationException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class LessonValidator implements Validator<Lesson> {
    private static final Integer LESSON_DURATION = 90;

    @Override
    public void validate(Lesson entity) {
        validateTimeDelta(entity.getTimeStart(), entity.getTimeEnd());
        validateDate(entity.getDate());
    }

    private void validateTimeDelta(LocalTime timeStart, LocalTime timeEnd) {
        if (Duration.between(timeStart, timeEnd).toMinutes() != LESSON_DURATION) {
            throw new InvalidLessonDurationException("Lesson duration is more or less than 90 minutes");
        }
    }

    private void validateDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidLessonDateException("Lesson date is before current date");
        }
    }
}
