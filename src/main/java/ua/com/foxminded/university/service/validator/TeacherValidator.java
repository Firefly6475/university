package ua.com.foxminded.university.service.validator;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.exception.InvalidBirthdayException;
import ua.com.foxminded.university.service.exception.InvalidEmailException;
import ua.com.foxminded.university.service.exception.InvalidNameException;
import ua.com.foxminded.university.service.exception.InvalidPasswordException;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@Slf4j
public class TeacherValidator implements Validator<Teacher> {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w-+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]{2,}");
    private static final int MIN_AGE = 20;

    @Override
    public void validate(Teacher entity) {
        validateEmail(entity.getEmail());
        validatePassword(entity.getPassword());
        validateName(entity.getName());
        validateBirthday(entity.getBirthday());
    }

    private void validateBirthday(LocalDate birthday) {
        log.info("Validating teacher birthday");
        if (Period.between(birthday, LocalDate.now()).getYears() < MIN_AGE) {
            log.error("Teacher birthday validation failed");
            throw new InvalidBirthdayException("Teacher age is lower than minimum of 20");
        }
    }

    private void validateEmail(String email) {
        log.info("Validating teacher email");
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            log.error("Teacher email validation failed");
            throw new InvalidEmailException("Email invalid");
        }
    }

    private void validatePassword(String password) {
        log.info("Validating teacher password");
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            log.error("Teacher password validation failed");
            throw new InvalidPasswordException(("Password must have at least 8 symbols, 1 upper-case symbol, 1 digit and 1 special character"));
        }
    }

    private void validateName(String name) {
        log.info("Validating teacher name");
        if (!NAME_PATTERN.matcher(name).matches()) {
            log.error("Teacher name validation failed");
            throw new InvalidNameException("Name is less than 2 symbols or contains numbers / non-word symbols");
        }
    }
}
