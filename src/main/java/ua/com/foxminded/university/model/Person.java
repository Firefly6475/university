package ua.com.foxminded.university.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
@SuperBuilder(setterPrefix = "with")
public class Person {
    @NonNull
    private final String id;
    private final String name;
    private final LocalDate birthday;
}
