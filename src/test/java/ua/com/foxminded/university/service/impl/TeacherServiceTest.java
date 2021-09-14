package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.exception.InvalidNameException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.Page;
import ua.com.foxminded.university.spring.dao.TeacherDao;

import java.time.LocalDate;
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
public class TeacherServiceTest {

    @Mock
    private TeacherDao teacherDao;

    @Mock
    private Validator<Teacher> validator;

    @InjectMocks
    @Spy
    private TeacherServiceImpl teacherService;

    @Test
    void registerTeacherShouldAddTeacher() {
        Teacher expectedTeacher = Teacher.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("gello@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("hello")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(teacherDao.findByEmail(expectedTeacher.getEmail()))
                .thenReturn(Optional.empty());
        doNothing().when(validator).validate(expectedTeacher);
        doNothing().when(teacherDao).save(expectedTeacher);

        teacherService.registerTeacher(expectedTeacher);

        verify(teacherDao).findByEmail(expectedTeacher.getEmail());
        verify(validator).validate(expectedTeacher);
        verify(teacherDao).save(expectedTeacher);
    }

    @Test
    void showAllTeachersShouldFindFirstPageWith2Teachers() {
        List<Teacher> teachers = new ArrayList<>();
        Page page = new Page(1, 2);

        when(teacherDao.findAll(page)).thenReturn(teachers);

        teachers = teacherService.showAllTeachers(page);

        verify(teacherDao).findAll(page);
    }

    @Test
    void findTeacherByIdShouldReturnTeacherWithSpecifiedID() {
        Teacher expectedTeacher = Teacher.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("hello@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("hello")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(teacherDao.findById(expectedTeacher.getId())).thenReturn(Optional.of(expectedTeacher));

        Teacher actualTeacher = teacherService.findTeacherById(expectedTeacher.getId());

        verify(teacherDao).findById(expectedTeacher.getId());

        assertEquals(expectedTeacher, actualTeacher);
    }

    @Test
    void deleteTeacherShouldRemoveTeacher() {
        Teacher expectedTeacher = Teacher.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("gello@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("hello")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(teacherDao.findById(expectedTeacher.getId())).thenReturn(Optional.of(expectedTeacher));
        doNothing().when(teacherDao).deleteById(expectedTeacher.getId());

        teacherService.deleteTeacher(expectedTeacher.getId());

        verify(teacherDao).findById(expectedTeacher.getId());
        verify(teacherDao).deleteById(expectedTeacher.getId());
    }

    @Test
    void editTeacherShouldUpdateTeacherIfEmailNotChanged() {
        Teacher teacherToEdit = Teacher.builder()
                .withId("Alexey")
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        Teacher editedTeacher = Teacher.builder()
                .withId("Alexey")
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("wor")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(teacherDao.findById(editedTeacher.getId())).thenReturn(Optional.of(teacherToEdit));
        when(teacherService.isEmailChanged(editedTeacher, teacherToEdit)).thenReturn(false);
        doNothing().when(validator).validate(editedTeacher);
        doNothing().when(teacherDao).update(editedTeacher);

        teacherService.editTeacher(editedTeacher);

        verify(teacherDao).findById(editedTeacher.getId());
        verify(teacherService).isEmailChanged(editedTeacher, teacherToEdit);
        verify(validator).validate(editedTeacher);
        verify(teacherDao).update(editedTeacher);
    }

    @Test
    void editTeacherShouldUpdateTeacherIfEmailChanged() {
        Teacher teacherToEdit = Teacher.builder()
                .withId("Alexey")
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        Teacher editedTeacher = Teacher.builder()
                .withId("Alexey")
                .withEmail("some_email@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("wor")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(teacherDao.findById(editedTeacher.getId())).thenReturn(Optional.of(teacherToEdit));
        when(teacherService.isEmailChanged(editedTeacher, teacherToEdit)).thenReturn(true);
        when(teacherDao.findByEmail(editedTeacher.getEmail())).thenReturn(Optional.empty());
        doNothing().when(validator).validate(editedTeacher);
        doNothing().when(teacherDao).update(editedTeacher);

        teacherService.editTeacher(editedTeacher);

        verify(teacherDao).findById(editedTeacher.getId());
        verify(teacherService).isEmailChanged(editedTeacher, teacherToEdit);
        verify(teacherDao).findByEmail(editedTeacher.getEmail());
        verify(validator).validate(editedTeacher);
        verify(teacherDao).update(editedTeacher);
    }

    @Test
    void registerTeacherShouldThrowEntityAlreadyExistExceptionIfEntityAlreadyExists() {
        Teacher expectedTeacher = Teacher.builder()
                .withId("ffgg")
                .withEmail("gello@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("hello")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(teacherDao.findByEmail(expectedTeacher.getEmail())).thenReturn(Optional.of(expectedTeacher));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> teacherService.registerTeacher(expectedTeacher));

        String expectedMessage = "Entity with specified email is already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(teacherDao).findByEmail(expectedTeacher.getEmail());
        verifyNoMoreInteractions(teacherDao);
    }

    @Test
    void findTeacherByIdShouldThrowEntityNotFoundExceptionIfNoSuchEntityExists() {
        String teacherId = "hello";

        when(teacherDao.findById(teacherId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> teacherService.findTeacherById("hello"));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(teacherDao).findById(teacherId);
        verifyNoMoreInteractions(teacherDao);
    }

    @Test
    void editTeacherShouldThrowEntityNotFoundExceptionIfNoSuchEntityExists() {
        Teacher teacher = Teacher.builder()
                .withId("hello")
                .withEmail("gello@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("hello")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(teacherDao.findById(teacher.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> teacherService.editTeacher(teacher));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(teacherDao).findById(teacher.getId());
        verifyNoMoreInteractions(teacherDao);
    }

    @Test
    void deleteTeacherShouldThrowEntityNotFoundExceptionIfNoSuchEntityExists() {
        String teacherId = "hello";

        when(teacherDao.findById(teacherId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> teacherService.deleteTeacher(teacherId));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(teacherDao).findById(teacherId);
        verifyNoMoreInteractions(teacherDao);
    }

    @Test
    void registerTeacherShouldThrowInvalidNameExceptionIfNameIsLessThan2Letters() {
        Teacher teacher = Teacher.builder()
                .withId("hello")
                .withEmail("gello@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("h")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(teacherDao.findByEmail(teacher.getEmail())).thenReturn(Optional.empty());
        doThrow(new InvalidNameException("Name is less than 2 symbols or contains numbers / non-word symbols"))
                .when(validator).validate(teacher);

        Exception exception = assertThrows(InvalidNameException.class, () -> teacherService.registerTeacher(teacher));

        String expectedMessage = "Name is less than 2 symbols or contains numbers / non-word symbols";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(teacherDao).findByEmail(teacher.getEmail());
        verify(validator).validate(teacher);
        verifyNoMoreInteractions(teacherDao);
    }

    @Test
    void findTeacherByEmailShouldReturnTeacherWithSpecifiedEmail() {
        Teacher expectedTeacher = Teacher.builder()
                .withId("hello")
                .withEmail("gello@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("hello")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();
        String email = "gello@gmail.com";

        when(teacherDao.findByEmail(email)).thenReturn(Optional.of(expectedTeacher));

        Teacher actualTeacher = teacherService.findTeacherByEmail(email);

        verify(teacherDao).findByEmail(email);

        assertEquals(expectedTeacher, actualTeacher);
    }

    @Test
    void findTeacherByEmailShouldThrowEntityNotFoundExceptionIfNoTeacherWithSpecifiedEmailInDb() {
        String teacherEmail = "hello@gmail.com";

        when(teacherDao.findByEmail(teacherEmail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> teacherService.findTeacherByEmail(teacherEmail));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(teacherDao).findByEmail(teacherEmail);
        verifyNoMoreInteractions(teacherDao);
    }

    @Test
    void editTeacherShouldThrowEntityAlreadyExistExceptionIfSpecifiedEmailAlreadyInDB() {
        Teacher teacherToEdit = Teacher.builder()
                .withId("hello")
                .withEmail("gello@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("hello")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        Teacher editedTeacher = Teacher.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("wor")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(teacherDao.findById(editedTeacher.getId())).thenReturn(Optional.of(teacherToEdit));
        when(teacherService.isEmailChanged(editedTeacher, teacherToEdit)).thenReturn(true);
        when(teacherDao.findByEmail(editedTeacher.getEmail())).thenReturn(Optional.of(teacherToEdit));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> teacherService.editTeacher(editedTeacher));

        String expectedMessage = "Specified email already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(teacherDao).findById(editedTeacher.getId());
        verify(teacherService).isEmailChanged(editedTeacher, teacherToEdit);
        verify(teacherDao).findByEmail(editedTeacher.getEmail());
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(teacherDao);
    }
}
