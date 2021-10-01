package ua.com.foxminded.university.service.validator;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.service.exception.InvalidNameException;

import java.util.regex.Pattern;

@Slf4j
public class FacultyValidator implements Validator<Faculty> {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z ]{4,30}");

    @Override
    public void validate(Faculty entity) {
        validateName(entity.getName());
    }

    private void validateName(String name) {
        log.info("Validating faculty name");
        if (!NAME_PATTERN.matcher(name).matches()) {
            log.error("Faculty name validation failed");
            throw new InvalidNameException("Faculty name is less than 4 or more than 30 symbols");
        }
    }
}
