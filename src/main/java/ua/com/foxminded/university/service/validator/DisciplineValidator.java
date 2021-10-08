package ua.com.foxminded.university.service.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.service.exception.InvalidNameException;

import java.util.regex.Pattern;

@Slf4j
@Component
public class DisciplineValidator implements Validator<Discipline> {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z ]{4,30}");

    @Override
    public void validate(Discipline entity) {
        validateName(entity.getName());
    }

    private void validateName(String name) {
        log.info("Validating discipline name");
        if (!NAME_PATTERN.matcher(name).matches()) {
            log.error("Discipline name validation failed");
            throw new InvalidNameException("Discipline name is less than 4 or more than 30 symbols");
        }
    }
}
