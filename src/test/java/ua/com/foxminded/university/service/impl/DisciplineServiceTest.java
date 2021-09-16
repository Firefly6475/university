package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.DisciplineService;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityIsNotEmptyException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.exception.InvalidNameException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.DisciplineDao;
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
public class DisciplineServiceTest {
    @Mock
    private DisciplineDao disciplineDao;

    @Mock
    private Validator<Discipline> validator;

    @Mock
    private TeacherDao teacherDao;

    @InjectMocks
    private DisciplineServiceImpl disciplineService;

    @Test
    void addDisciplineShouldSaveDiscipline() {
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Java")
                .build();

        when(disciplineDao.findByName(discipline.getName())).thenReturn(Optional.empty());
        doNothing().when(validator).validate(discipline);
        doNothing().when(disciplineDao).save(discipline);

        disciplineService.addDiscipline(discipline);

        verify(disciplineDao).findByName(discipline.getName());
        verify(validator).validate(discipline);
        verify(disciplineDao).save(discipline);
    }

    @Test
    void addDisciplineShouldThrowEntityAlreadyExistExceptionIfEntityWithSpecifiedNameAlreadyExists() {
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Java")
                .build();

        when(disciplineDao.findByName(discipline.getName())).thenReturn(Optional.of(discipline));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> disciplineService.addDiscipline(discipline));

        String expectedMessage = "Entity with specified name is already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findByName(discipline.getName());
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(disciplineDao);
    }

    @Test
    void addDisciplineShouldThrowInvalidNameExceptionIfEntityHaveNameWithLessThan4Symbols() {
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("hi")
                .build();

        when(disciplineDao.findByName(discipline.getName())).thenReturn(Optional.empty());
        doThrow(new InvalidNameException("Discipline name is less than 4 or more than 30 symbols")).when(validator).validate(discipline);

        Exception exception = assertThrows(InvalidNameException.class, () -> disciplineService.addDiscipline(discipline));

        String expectedMessage = "Discipline name is less than 4 or more than 30 symbols";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findByName(discipline.getName());
        verify(validator).validate(discipline);
        verifyNoMoreInteractions(disciplineDao);
    }

    @Test
    void findDisciplineByIdShouldReturnEntityWithSpecifiedId() {
        Discipline expectedDiscipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Java")
                .build();

        when(disciplineDao.findById(expectedDiscipline.getId())).thenReturn(Optional.of(expectedDiscipline));

        Discipline actualDiscipline = disciplineService.findDisciplineById(expectedDiscipline.getId());
        assertEquals(expectedDiscipline, actualDiscipline);

        verify(disciplineDao).findById(expectedDiscipline.getId());
    }

    @Test
    void findDisciplineByIdShouldThrowEntityNotFoundExceptionIfEntityWithSpecifiedIdIsNotExists() {
        String disciplineId = "hello";

        when(disciplineDao.findById(disciplineId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> disciplineService.findDisciplineById(disciplineId));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findById(disciplineId);
    }

    @Test
    void findDisciplineByNameShouldReturnEntityWithSpecifiedId() {
        Discipline expectedDiscipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Java")
                .build();

        when(disciplineDao.findByName(expectedDiscipline.getName())).thenReturn(Optional.of(expectedDiscipline));

        Discipline actualDiscipline = disciplineService.findDisciplineByName(expectedDiscipline.getName());
        assertEquals(expectedDiscipline, actualDiscipline);

        verify(disciplineDao).findByName(expectedDiscipline.getName());
    }

    @Test
    void findDisciplineByNameShouldThrowEntityNotFoundExceptionIfEntityWithSpecifiedIdIsNotExists() {
        String disciplineName = "Dart";

        when(disciplineDao.findByName(disciplineName)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> disciplineService.findDisciplineByName(disciplineName));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findByName(disciplineName);
    }

    @Test
    void showAllDisciplinesShouldReturnFirstPageWith2Entities() {
        List<Discipline> disciplines = new ArrayList<>();
        Page page = new Page(1, 2);

        when(disciplineDao.findAll(page)).thenReturn(disciplines);

        disciplines = disciplineService.showAllDisciplines(page);

        verify(disciplineDao).findAll(page);
    }

    @Test
    void deleteDisciplineShouldRemoveDisciplineFromDB() {
        Discipline expectedDiscipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Java")
                .withTeachers(new ArrayList<>())
                .build();

        when(disciplineDao.findById(expectedDiscipline.getId())).thenReturn(Optional.of(expectedDiscipline));
        doNothing().when(disciplineDao).deleteById(expectedDiscipline.getId());

        disciplineService.deleteDiscipline(expectedDiscipline.getId());

        verify(disciplineDao).findById(expectedDiscipline.getId());
        verify(disciplineDao).deleteById(expectedDiscipline.getId());
    }

    @Test
    void deleteDisciplineShouldThrowEntityNotFoundExceptionIfThereIsNoEntityWithSpecifiedId() {
        String disciplineId = "hello";

        when(disciplineDao.findById(disciplineId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> disciplineService.deleteDiscipline(disciplineId));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findById(disciplineId);
        verifyNoMoreInteractions(disciplineDao);
    }

    @Test
    void deleteDisciplineShouldThrowEntityIsNotEmptyExceptionIfDisciplineHaveTeachers() {
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Java")
                .withTeachers(new ArrayList<>())
                .build();
        Teacher teacher = Teacher.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();
        discipline.addTeacher(teacher);
        String disciplineId = discipline.getId();

        when(disciplineDao.findById(disciplineId)).thenReturn(Optional.of(discipline));

        Exception exception = assertThrows(EntityIsNotEmptyException.class, () -> disciplineService.deleteDiscipline(disciplineId));

        String expectedMessage = "Can't delete non-empty entity";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findById(disciplineId);
    }

    @Test
    void editDisciplineShouldUpdateDisciplineInDb() {
        Discipline disciplineToEdit = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Java")
                .withTeachers(new ArrayList<>())
                .build();

        Discipline editedDiscipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Python")
                .withTeachers(new ArrayList<>())
                .build();

        when(disciplineDao.findById(editedDiscipline.getId())).thenReturn(Optional.of(disciplineToEdit));
        when(disciplineDao.findByName(editedDiscipline.getName())).thenReturn(Optional.empty());
        doNothing().when(validator).validate(editedDiscipline);
        doNothing().when(disciplineDao).update(editedDiscipline);

        disciplineService.editDiscipline(editedDiscipline);

        verify(disciplineDao).findById(editedDiscipline.getId());
        verify(disciplineDao).findByName(editedDiscipline.getName());
        verify(validator).validate(editedDiscipline);
        verify(disciplineDao).update(editedDiscipline);
    }

    @Test
    void editDisciplineShouldThrowEntityNotFoundExceptionIfThereIsNoDisciplineWithSpecifiedId() {
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Java")
                .withTeachers(new ArrayList<>())
                .build();

        when(disciplineDao.findById(discipline.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> disciplineService.editDiscipline(discipline));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findById(discipline.getId());
        verifyNoMoreInteractions(disciplineDao);
        verifyNoInteractions(validator);
    }

    @Test
    void editDisciplineShouldThrowEntityAlreadyExistExceptionIfSpecifiedNameAlreadyExists() {
        Discipline disciplineToEdit = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Java")
                .withTeachers(new ArrayList<>())
                .build();

        Discipline editedDiscipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Python")
                .withTeachers(new ArrayList<>())
                .build();

        when(disciplineDao.findById(editedDiscipline.getId())).thenReturn(Optional.of(disciplineToEdit));
        when(disciplineDao.findByName(editedDiscipline.getName())).thenReturn(Optional.of(disciplineToEdit));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> disciplineService.editDiscipline(editedDiscipline));

        String expectedMessage = "Specified name already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findById(editedDiscipline.getId());
        verify(disciplineDao).findByName(editedDiscipline.getName());
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(disciplineDao);
    }

    @Test
    void addTeacherToDisciplineShouldAddTeacherToDiscipline() {
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("someDiscipline")
                .withTeachers(new ArrayList<>())
                .build();
        Teacher teacher = Teacher.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();

        String disciplineId = discipline.getId();
        String teacherId = teacher.getId();

        when(disciplineDao.findById(disciplineId)).thenReturn(Optional.of(discipline));
        when(teacherDao.findById(teacherId)).thenReturn(Optional.of(teacher));
        doNothing().when(disciplineDao).addTeacherToDiscipline(disciplineId, teacherId);

        disciplineService.addTeacherToDiscipline(disciplineId, teacherId);

        verify(disciplineDao).findById(disciplineId);
        verify(teacherDao).findById(teacherId);
        verify(disciplineDao).addTeacherToDiscipline(disciplineId, teacherId);
    }

    @Test
    void addTeacherToDisciplineShouldThrowEntityNoFoundExceptionIfSpecifiedDisciplineNotExists() {
        String disciplineId = "hello";
        String teacherId = "world";

        when(disciplineDao.findById(disciplineId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> disciplineService.addTeacherToDiscipline(disciplineId, teacherId));

        String expectedMessage = "Specified discipline not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findById(disciplineId);
        verifyNoMoreInteractions(disciplineDao);
        verifyNoInteractions(teacherDao);
    }

    @Test
    void addTeacherToDisciplineShouldThrowEntityNoFoundExceptionIfSpecifiedTeacherNotExists() {
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("someDiscipline")
                .withTeachers(new ArrayList<>())
                .build();

        String teacherId = "world";
        String disciplineId = discipline.getId();

        when(disciplineDao.findById(disciplineId)).thenReturn(Optional.of(discipline));
        when(teacherDao.findById(teacherId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> disciplineService.addTeacherToDiscipline(disciplineId, teacherId));

        String expectedMessage = "Specified teacher not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findById(disciplineId);
        verify(teacherDao).findById(teacherId);
        verifyNoMoreInteractions(disciplineDao);
        verifyNoMoreInteractions(teacherDao);
    }

    @Test
    void addTeacherToDisciplineShouldThrowEntityAlreadyExistExceptionIfSpecifiedTeacherAlreadyInDiscipline() {
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("someDiscipline")
                .withTeachers(new ArrayList<>())
                .build();
        Teacher teacher = Teacher.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();
        discipline.addTeacher(teacher);

        String disciplineId = discipline.getId();
        String teacherId = teacher.getId();

        when(disciplineDao.findById(disciplineId)).thenReturn(Optional.of(discipline));
        when(teacherDao.findById(teacherId)).thenReturn(Optional.of(teacher));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> disciplineService.addTeacherToDiscipline(disciplineId, teacherId));

        String expectedMessage = "Specified teacher already in discipline";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findById(disciplineId);
        verify(teacherDao).findById(teacherId);
        verifyNoMoreInteractions(disciplineDao);
        verifyNoMoreInteractions(teacherDao);
    }

    @Test
    void removeTeacherFromDisciplineShouldRemoveTeacherFromDiscipline() {
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("someDiscipline")
                .withTeachers(new ArrayList<>())
                .build();
        Teacher teacher = Teacher.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();
        discipline.addTeacher(teacher);

        String disciplineId = discipline.getId();
        String teacherId = teacher.getId();

        when(disciplineDao.findById(disciplineId)).thenReturn(Optional.of(discipline));
        when(teacherDao.findById(teacherId)).thenReturn(Optional.of(teacher));
        doNothing().when(disciplineDao).removeTeacherFromDiscipline(disciplineId, teacherId);

        disciplineService.removeTeacherFromDiscipline(disciplineId, teacherId);

        verify(disciplineDao).findById(disciplineId);
        verify(teacherDao).findById(teacherId);
        verify(disciplineDao).removeTeacherFromDiscipline(disciplineId, teacherId);
    }

    @Test
    void removeTeacherFromDisciplineShouldThrowEntityNoFoundExceptionIfSpecifiedDisciplineNotExists() {
        String disciplineId = "hello";
        String teacherId = "world";

        when(disciplineDao.findById(disciplineId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> disciplineService.removeTeacherFromDiscipline(disciplineId, teacherId));

        String expectedMessage = "Specified discipline not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findById(disciplineId);
        verifyNoMoreInteractions(disciplineDao);
        verifyNoInteractions(teacherDao);
    }

    @Test
    void removeTeacherFromDisciplineShouldThrowEntityNoFoundExceptionIfSpecifiedTeacherNotExists() {
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("someDiscipline")
                .withTeachers(new ArrayList<>())
                .build();
        String teacherId = "world";
        String disciplineId = discipline.getId();

        when(disciplineDao.findById(disciplineId)).thenReturn(Optional.of(discipline));
        when(teacherDao.findById(teacherId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> disciplineService.removeTeacherFromDiscipline(disciplineId, teacherId));

        String expectedMessage = "Specified teacher not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findById(disciplineId);
        verify(teacherDao).findById(teacherId);
        verifyNoMoreInteractions(disciplineDao);
        verifyNoMoreInteractions(teacherDao);
    }

    @Test
    void removeTeacherFromDisciplineShouldThrowEntityAlreadyExistExceptionIfSpecifiedTeacherIsNotInADiscipline() {
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("someDiscipline")
                .withTeachers(new ArrayList<>())
                .build();
        Teacher teacher = Teacher.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();

        String disciplineId = discipline.getId();
        String teacherId = teacher.getId();

        when(disciplineDao.findById(disciplineId)).thenReturn(Optional.of(discipline));
        when(teacherDao.findById(teacherId)).thenReturn(Optional.of(teacher));

        Exception exception = assertThrows(EntityNotFoundException.class, () -> disciplineService.removeTeacherFromDiscipline(disciplineId, teacherId));

        String expectedMessage = "Specified teacher is not in a discipline";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findById(disciplineId);
        verify(teacherDao).findById(teacherId);
        verifyNoMoreInteractions(disciplineDao);
        verifyNoMoreInteractions(teacherDao);
    }
}
