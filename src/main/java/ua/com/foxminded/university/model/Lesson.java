package ua.com.foxminded.university.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class Lesson {
    @NonNull
    private final String id;
    private final Discipline discipline;
    private final Audience audience;
    private final LessonType lessonType;
    private final Group group;
    private final Teacher teacher;
    private final LocalDate date;
    private final LocalTime timeStart;
    private final LocalTime timeEnd;
}
