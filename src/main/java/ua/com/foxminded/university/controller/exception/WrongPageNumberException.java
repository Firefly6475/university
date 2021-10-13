package ua.com.foxminded.university.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Specified page doesn't exists")
public class WrongPageNumberException extends RuntimeException {

}
