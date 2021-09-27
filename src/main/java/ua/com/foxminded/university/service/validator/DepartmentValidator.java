package ua.com.foxminded.university.service.validator;

import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.service.exception.InvalidNameException;

import java.util.regex.Pattern;

public class DepartmentValidator implements Validator<Department> {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z ]{4,30}");

    @Override
    public void validate(Department entity) {
        validateName(entity.getName());
    }

    private void validateName(String name) {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new InvalidNameException("Department name is less than 4 or more than 30 symbols");
        }
    }
}
