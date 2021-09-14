package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.exception.InvalidNameException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.AudienceDao;
import ua.com.foxminded.university.spring.dao.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AudienceServiceTest {
    @Mock
    private AudienceDao audienceDao;

    @Mock
    private Validator<Audience> validator;

    @InjectMocks
    @Spy
    private AudienceServiceImpl audienceService;

    @Test
    void addAudienceShouldSaveAudience() {
        Audience expectedAudience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withNumber(238)
                .withFloor(2)
                .build();

        when(audienceDao.findByNumber(expectedAudience.getNumber()))
                .thenReturn(Optional.empty());
        doNothing().when(validator).validate(expectedAudience);
        doNothing().when(audienceDao).save(expectedAudience);

        audienceService.addAudience(expectedAudience);

        verify(audienceDao).findByNumber(expectedAudience.getNumber());
        verify(validator).validate(expectedAudience);
        verify(audienceDao).save(expectedAudience);
    }

    @Test
    void showAllAudiencesShouldFindFirstPageWith2Audiences() {
        List<Audience> audiences = new ArrayList<>();
        Page page = new Page(1, 2);

        when(audienceDao.findAll(page)).thenReturn(audiences);

        audiences = audienceService.showAllAudiences(page);

        verify(audienceDao).findAll(page);
    }

    @Test
    void findAudienceByIdShouldReturnAudienceWithSpecifiedID() {
        Audience expectedAudience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withNumber(238)
                .withFloor(2)
                .build();

        when(audienceDao.findById(expectedAudience.getId())).thenReturn(Optional.of(expectedAudience));

        Audience actualAudience = audienceService.findAudienceById(expectedAudience.getId());

        verify(audienceDao).findById(expectedAudience.getId());

        assertEquals(expectedAudience, actualAudience);
    }

    @Test
    void deleteAudienceShouldRemoveAudience() {
        Audience expectedAudience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withNumber(238)
                .withFloor(2)
                .build();

        when(audienceDao.findById(expectedAudience.getId())).thenReturn(Optional.of(expectedAudience));
        doNothing().when(audienceDao).deleteById(expectedAudience.getId());

        audienceService.deleteAudience(expectedAudience.getId());

        verify(audienceDao).findById(expectedAudience.getId());
        verify(audienceDao).deleteById(expectedAudience.getId());
    }

    @Test
    void editAudienceShouldUpdateAudienceIfNumberNotChanged() {
        Audience audienceToEdit = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withNumber(238)
                .withFloor(2)
                .build();

        Audience editedAudience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withNumber(238)
                .withFloor(2)
                .build();

        when(audienceDao.findById(editedAudience.getId())).thenReturn(Optional.of(audienceToEdit));
        when(audienceService.isNumberChanged(editedAudience, audienceToEdit)).thenReturn(false);
        doNothing().when(validator).validate(editedAudience);
        doNothing().when(audienceDao).update(editedAudience);

        audienceService.editAudience(editedAudience);

        verify(audienceDao).findById(editedAudience.getId());
        verify(audienceService).isNumberChanged(editedAudience, audienceToEdit);
        verify(validator).validate(editedAudience);
        verify(audienceDao).update(editedAudience);
    }

    @Test
    void editAudienceShouldUpdateAudienceIfNumberChanged() {
        Audience audienceToEdit = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withNumber(238)
                .withFloor(2)
                .build();

        Audience editedAudience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withNumber(252)
                .withFloor(2)
                .build();

        when(audienceDao.findById(editedAudience.getId())).thenReturn(Optional.of(audienceToEdit));
        when(audienceService.isNumberChanged(editedAudience, audienceToEdit)).thenReturn(true);
        when(audienceDao.findByNumber(editedAudience.getNumber())).thenReturn(Optional.empty());
        doNothing().when(validator).validate(editedAudience);
        doNothing().when(audienceDao).update(editedAudience);

        audienceService.editAudience(editedAudience);

        verify(audienceDao).findById(editedAudience.getId());
        verify(audienceService).isNumberChanged(editedAudience, audienceToEdit);
        verify(audienceDao).findByNumber(editedAudience.getNumber());
        verify(validator).validate(editedAudience);
        verify(audienceDao).update(editedAudience);
    }

    @Test
    void addAudienceShouldThrowEntityAlreadyExistExceptionIfEntityAlreadyExists() {
        Audience expectedAudience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withNumber(238)
                .withFloor(2)
                .build();

        when(audienceDao.findByNumber(expectedAudience.getNumber())).thenReturn(Optional.of(expectedAudience));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> audienceService.addAudience(expectedAudience));

        String expectedMessage = "Entity with specified number is already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(audienceDao).findByNumber(expectedAudience.getNumber());
        verifyNoMoreInteractions(audienceDao);
    }

    @Test
    void findAudienceByIdShouldThrowEntityNotFoundExceptionIfNoSuchEntityExists() {
        String audienceId = "Alexey";

        when(audienceDao.findById(audienceId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> audienceService.findAudienceById("Alexey"));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(audienceDao).findById(audienceId);
        verifyNoMoreInteractions(audienceDao);
    }

    @Test
    void editAudienceShouldThrowEntityNotFoundExceptionIfNoSuchEntityExists() {
        Audience audience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withNumber(238)
                .withFloor(2)
                .build();

        when(audienceDao.findById(audience.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> audienceService.editAudience(audience));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(audienceDao).findById(audience.getId());
        verifyNoMoreInteractions(audienceDao);
    }

    @Test
    void deleteAudienceShouldThrowEntityNotFoundExceptionIfNoSuchEntityExists() {
        String audienceId = "Alexey";

        when(audienceDao.findById(audienceId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> audienceService.deleteAudience(audienceId));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(audienceDao).findById(audienceId);
        verifyNoMoreInteractions(audienceDao);
    }

    @Test
    void addAudienceShouldThrowInvalidAudienceNumberExceptionIfNumberIsZero() {
        Audience audience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withNumber(0)
                .withFloor(2)
                .build();

        when(audienceDao.findByNumber(audience.getNumber())).thenReturn(Optional.empty());
        doThrow(new InvalidNameException("Number is starting with zero, negative or have less than 3 symbols"))
                .when(validator).validate(audience);

        Exception exception = assertThrows(InvalidNameException.class, () -> audienceService.addAudience(audience));

        String expectedMessage = "Number is starting with zero, negative or have less than 3 symbols";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(audienceDao).findByNumber(audience.getNumber());
        verify(validator).validate(audience);
        verifyNoMoreInteractions(audienceDao);
    }

    @Test
    void findAudienceByNumberShouldReturnAudienceWithSpecifiedNumber() {
        Audience expectedAudience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withNumber(238)
                .withFloor(2)
                .build();
        Integer number = 238;

        when(audienceDao.findByNumber(number)).thenReturn(Optional.of(expectedAudience));

        Audience actualAudience = audienceService.findAudienceByNumber(number);

        verify(audienceDao).findByNumber(number);

        assertEquals(expectedAudience, actualAudience);
    }

    @Test
    void findAudienceByNumberShouldThrowEntityNotFoundExceptionIfNoAudienceWithSpecifiedNumberInDb() {
        Integer audienceNumber = 342;

        when(audienceDao.findByNumber(audienceNumber)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> audienceService.findAudienceByNumber(audienceNumber));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(audienceDao).findByNumber(audienceNumber);
        verifyNoMoreInteractions(audienceDao);
    }

    @Test
    void editAudienceShouldThrowEntityAlreadyExistExceptionIfSpecifiedNumberAlreadyInDB() {
        Audience audienceToEdit = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withNumber(238)
                .withFloor(2)
                .build();

        Audience editedAudience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withNumber(258)
                .withFloor(2)
                .build();

        when(audienceDao.findById(editedAudience.getId())).thenReturn(Optional.of(audienceToEdit));
        when(audienceService.isNumberChanged(editedAudience, audienceToEdit)).thenReturn(true);
        when(audienceDao.findByNumber(editedAudience.getNumber())).thenReturn(Optional.of(audienceToEdit));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> audienceService.editAudience(editedAudience));

        String expectedMessage = "Specified number already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(audienceDao).findById(editedAudience.getId());
        verify(audienceService).isNumberChanged(editedAudience, audienceToEdit);
        verify(audienceDao).findByNumber(editedAudience.getNumber());
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(audienceDao);
    }
}
