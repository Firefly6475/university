package ua.com.foxminded.university.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class Audience {
    @NonNull
    private final String id;
    private final Integer number;
    private final Integer floor;
}
