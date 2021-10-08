package ua.com.foxminded.university.service.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.exception.InvalidCourseException;
import ua.com.foxminded.university.service.exception.InvalidNameException;

import java.util.regex.Pattern;

@Slf4j
@Component
public class GroupValidator implements Validator<Group> {
    private final static Pattern GROUP_NAME_PATTERN = Pattern.compile("[A-Z]{2}-[0-9]{2}");
    private final static int MAX_COURSE = 4;

    @Override
    public void validate(Group entity) {
        validateName(entity.getName());
        validateCourse(entity.getCourse());
    }

    private void validateName(String name) {
        log.info("Validating group name");
        if (!GROUP_NAME_PATTERN.matcher(name).matches()) {
            log.error("Group name validation failed");
            throw new InvalidNameException("Group name format invalid");
        }
    }

    private void validateCourse(Integer course) {
        log.info("Validating group course");
        if (course > MAX_COURSE || course <= 0) {
            log.error("Group course validation failed");
            throw new InvalidCourseException("Group course is above maximum, zero or negative");
        }
    }
}
