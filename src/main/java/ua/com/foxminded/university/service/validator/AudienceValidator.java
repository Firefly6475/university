package ua.com.foxminded.university.service.validator;

import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.service.exception.InvalidAudienceFloorException;
import ua.com.foxminded.university.service.exception.InvalidAudienceNumberException;
import ua.com.foxminded.university.service.exception.InvalidNumberOnFloorException;

import java.util.regex.Pattern;

public class AudienceValidator implements Validator<Audience> {
    private static final Pattern AUDIENCE_NUMBER_PATTERN = Pattern.compile("[1-9][0-9]{2}");
    private static final Pattern AUDIENCE_FLOOR_PATTERN = Pattern.compile("[1-5]");

    @Override
    public void validate(Audience entity) {
        validateNumber(entity.getNumber());
        validateFloor(entity.getFloor());
        validateNumberOnFloor(entity.getNumber(), entity.getFloor());
    }

    private void validateNumber(Integer number) {
        if(!AUDIENCE_NUMBER_PATTERN.matcher(number.toString()).matches()) {
            throw new InvalidAudienceNumberException("Number is starting with zero, negative or have less than 3 symbols");
        }
    }

    private void validateFloor(Integer floor) {
        if (!AUDIENCE_FLOOR_PATTERN.matcher(floor.toString()).matches()) {
            throw new InvalidAudienceFloorException("Floor is zero, negative or don't exist");
        }
    }

    private void validateNumberOnFloor(Integer number, Integer floor) {
        if (number / (floor * 100) != 1) {
            throw new InvalidNumberOnFloorException("This audience can't be on specified floor");
        }
    }
}
