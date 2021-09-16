package ua.com.foxminded.university.service.validator;

import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.service.exception.InvalidNameException;

import java.util.regex.Pattern;

public class DisciplineValidator implements Validator<Discipline> {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z ]{4,30}");

    @Override
    public void validate(Discipline entity) {
        validateName(entity.getName());
    }

    private void validateName(String name) {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new InvalidNameException("Discipline name is less than 4 or more than 30 symbols");
        }
    }
}
