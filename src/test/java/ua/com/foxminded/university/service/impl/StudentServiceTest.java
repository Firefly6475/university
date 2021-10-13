package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.exception.InvalidNameException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.Page;
import ua.com.foxminded.university.spring.dao.StudentDao;

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
public class StudentServiceTest {
    @Mock
    private StudentDao studentDao;

    @Mock
    private Validator<Student> validator;

    @InjectMocks
    @Spy
    private StudentServiceImpl studentService;

    @Test
    void registerStudentShouldAddStudent() {
        Student expectedStudent = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(studentDao.findByEmail(expectedStudent.getEmail()))
                .thenReturn(Optional.empty());
        doNothing().when(validator).validate(expectedStudent);
        doNothing().when(studentDao).save(expectedStudent);

        studentService.registerStudent(expectedStudent);

        verify(studentDao).findByEmail(expectedStudent.getEmail());
        verify(validator).validate(expectedStudent);
        verify(studentDao).save(expectedStudent);
    }

    @Test
    void showAllStudentsShouldReturnAllStudents() {
        List<Student> students = new ArrayList<>();

        when(studentDao.findAll()).thenReturn(students);

        students = studentService.showAllStudents();

        verify(studentDao).findAll();
    }

    @Test
    void showAllStudentsShouldFindFirstPageWith10Students() {
        List<Student> students = new ArrayList<>();
        int pageNumber = 1;
        Page page = new Page(pageNumber, 10);

        when(studentService.generatePage(pageNumber)).thenReturn(page);
        when(studentDao.findAll(page)).thenReturn(students);

        students = studentService.showAllStudents(pageNumber);

        verify(studentService).generatePage(pageNumber);
        verify(studentDao).findAll(page);
    }

    @Test
    void findStudentByIdShouldReturnStudentWithSpecifiedID() {
        Student expectedStudent = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("world")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(studentDao.findById(expectedStudent.getId())).thenReturn(Optional.of(expectedStudent));

        Student actualStudent = studentService.findStudentById(expectedStudent.getId());

        verify(studentDao).findById(expectedStudent.getId());

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void deleteStudentShouldRemoveStudent() {
        Student expectedStudent = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(studentDao.findById(expectedStudent.getId())).thenReturn(Optional.of(expectedStudent));
        doNothing().when(studentDao).deleteById(expectedStudent.getId());

        studentService.deleteStudent(expectedStudent.getId());

        verify(studentDao).findById(expectedStudent.getId());
        verify(studentDao).deleteById(expectedStudent.getId());
    }

    @Test
    void editStudentShouldUpdateStudentIfEmailNotChanged() {
        Student studentToEdit = Student.builder()
                .withId("Alexey")
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        Student editedStudent = Student.builder()
                .withId("Alexey")
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("wor")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(studentDao.findById(editedStudent.getId())).thenReturn(Optional.of(studentToEdit));
        when(studentService.isEmailChanged(editedStudent, studentToEdit)).thenReturn(false);
        doNothing().when(validator).validate(editedStudent);
        doNothing().when(studentDao).update(editedStudent);

        studentService.editStudent(editedStudent);

        verify(studentDao).findById(editedStudent.getId());
        verify(studentService).isEmailChanged(editedStudent, studentToEdit);
        verify(validator).validate(editedStudent);
        verify(studentDao).update(editedStudent);
    }

    @Test
    void editStudentShouldUpdateStudentIfNumberChanged() {
        Student studentToEdit = Student.builder()
                .withId("Alexey")
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        Student editedStudent = Student.builder()
                .withId("Alexey")
                .withEmail("some_email@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("wor")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(studentDao.findById(editedStudent.getId())).thenReturn(Optional.of(studentToEdit));
        when(studentService.isEmailChanged(editedStudent, studentToEdit)).thenReturn(true);
        when(studentDao.findByEmail(editedStudent.getEmail())).thenReturn(Optional.empty());
        doNothing().when(validator).validate(editedStudent);
        doNothing().when(studentDao).update(editedStudent);

        studentService.editStudent(editedStudent);

        verify(studentDao).findById(editedStudent.getId());
        verify(studentService).isEmailChanged(editedStudent, studentToEdit);
        verify(studentDao).findByEmail(editedStudent.getEmail());
        verify(validator).validate(editedStudent);
        verify(studentDao).update(editedStudent);
    }

    @Test
    void registerStudentShouldThrowEntityAlreadyExistExceptionIfEntityAlreadyExists() {
        Student expectedStudent = Student.builder()
                .withId("ffgg")
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(studentDao.findByEmail(expectedStudent.getEmail())).thenReturn(Optional.of(expectedStudent));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> studentService.registerStudent(expectedStudent));

        String expectedMessage = "Entity with specified email is already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(studentDao).findByEmail(expectedStudent.getEmail());
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void findStudentByIdShouldThrowEntityNotFoundExceptionIfNoSuchEntityExists() {
        String studentId = "Alexey";

        when(studentDao.findById(studentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> studentService.findStudentById("Alexey"));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(studentDao).findById(studentId);
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void editStudentShouldThrowEntityNotFoundExceptionIfNoSuchEntityExists() {
        Student student = Student.builder()
                .withId("Alexey")
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(studentDao.findById(student.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> studentService.editStudent(student));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(studentDao).findById(student.getId());
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void deleteStudentShouldThrowEntityNotFoundExceptionIfNoSuchEntityExists() {
        String studentId = "Alexey";

        when(studentDao.findById(studentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> studentService.deleteStudent(studentId));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(studentDao).findById(studentId);
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void registerStudentShouldThrowInvalidNameExceptionIfNameIsLessThan2Letters() {
        Student student = Student.builder()
                .withId("Alexey")
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("h")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(studentDao.findByEmail(student.getEmail())).thenReturn(Optional.empty());
        doThrow(new InvalidNameException("Name is less than 2 symbols or contains numbers / non-word symbols"))
                .when(validator).validate(student);

        Exception exception = assertThrows(InvalidNameException.class, () -> studentService.registerStudent(student));

        String expectedMessage = "Name is less than 2 symbols or contains numbers / non-word symbols";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(studentDao).findByEmail(student.getEmail());
        verify(validator).validate(student);
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void findStudentByEmailShouldReturnStudentWithSpecifiedEmail() {
        Student expectedStudent = Student.builder()
                .withId("Alexey")
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();
        String email = "world@gmail.com";

        when(studentDao.findByEmail(email)).thenReturn(Optional.of(expectedStudent));

        Student actualStudent = studentService.findStudentByEmail(email);

        verify(studentDao).findByEmail(email);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void findStudentByEmailShouldThrowEntityNotFoundExceptionIfNoStudentWithSpecifiedEmailInDb() {
        String studentEmail = "world@gmail.com";

        when(studentDao.findByEmail(studentEmail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> studentService.findStudentByEmail(studentEmail));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(studentDao).findByEmail(studentEmail);
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void editStudentShouldThrowEntityAlreadyExistExceptionIfSpecifiedEmailAlreadyInDB() {
        Student studentToEdit = Student.builder()
                .withId("Alexey")
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("Alexey")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        Student editedStudent = Student.builder()
                .withId("Alexey")
                .withEmail("world@gmail.com")
                .withPassword("mYP@sSw0rd")
                .withName("wor")
                .withBirthday(LocalDate.parse("1997-01-01"))
                .build();

        when(studentDao.findById(editedStudent.getId())).thenReturn(Optional.of(studentToEdit));
        when(studentService.isEmailChanged(editedStudent, studentToEdit)).thenReturn(true);
        when(studentDao.findByEmail(editedStudent.getEmail())).thenReturn(Optional.of(studentToEdit));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> studentService.editStudent(editedStudent));

        String expectedMessage = "Specified email already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(studentDao).findById(editedStudent.getId());
        verify(studentService).isEmailChanged(editedStudent, studentToEdit);
        verify(studentDao).findByEmail(editedStudent.getEmail());
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(studentDao);
    }
}
