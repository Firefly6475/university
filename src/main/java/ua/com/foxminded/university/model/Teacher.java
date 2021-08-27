package ua.com.foxminded.university.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@SuperBuilder
public class Teacher extends Person {
    private final Integer salary;
}
