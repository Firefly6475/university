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
public class Group {
    @NonNull
    private final String id;
    private final String name;
    @Singular
    private final List<Student> students;

    public void addStudent(@NonNull Student student) {
        students.add(student);
    }
}
