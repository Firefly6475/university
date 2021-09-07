package ua.com.foxminded.university.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@Builder(setterPrefix = "with")
public class Discipline {
    @NonNull
    private final String id;
    private final String name;
    private final List<Teacher> teachers;

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }
}
