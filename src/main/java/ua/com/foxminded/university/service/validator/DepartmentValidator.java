package ua.com.foxminded.university.service.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.service.exception.InvalidNameException;

import java.util.regex.Pattern;

@Slf4j
@Component
public class DepartmentValidator implements Validator<Department> {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z ]{4,30}");

    @Override
    public void validate(Department entity) {
        validateName(entity.getName());
    }

    private void validateName(String name) {
        log.info("Validating department name");
        if (!NAME_PATTERN.matcher(name).matches()) {
            log.error("Department name validation failed");
            throw new InvalidNameException("Department name is less than 4 or more than 30 symbols");
        }
    }
}
