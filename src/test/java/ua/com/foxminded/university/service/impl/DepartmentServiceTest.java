package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityIsNotEmptyException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.exception.InvalidNameException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.DepartmentDao;
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
public class DepartmentServiceTest {
    @Mock
    private DepartmentDao departmentDao;

    @Mock
    private Validator<Department> validator;

    @Mock
    private TeacherDao teacherDao;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    void addDepartmentShouldSaveDepartment() {
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .build();

        when(departmentDao.findByName(department.getName())).thenReturn(Optional.empty());
        doNothing().when(validator).validate(department);
        doNothing().when(departmentDao).save(department);

        departmentService.addDepartment(department);

        verify(departmentDao).findByName(department.getName());
        verify(validator).validate(department);
        verify(departmentDao).save(department);
    }

    @Test
    void addDepartmentShouldThrowEntityAlreadyExistExceptionIfEntityWithSpecifiedNameAlreadyExists() {
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .build();

        when(departmentDao.findByName(department.getName())).thenReturn(Optional.of(department));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> departmentService.addDepartment(department));

        String expectedMessage = "Entity with specified name is already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findByName(department.getName());
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(departmentDao);
    }

    @Test
    void addDepartmentShouldThrowInvalidNameExceptionIfEntityHaveNameWithLessThan4Symbols() {
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("hi")
                .build();

        when(departmentDao.findByName(department.getName())).thenReturn(Optional.empty());
        doThrow(new InvalidNameException("Department name is less than 4 or more than 30 symbols")).when(validator).validate(department);

        Exception exception = assertThrows(InvalidNameException.class, () -> departmentService.addDepartment(department));

        String expectedMessage = "Department name is less than 4 or more than 30 symbols";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findByName(department.getName());
        verify(validator).validate(department);
        verifyNoMoreInteractions(departmentDao);
    }

    @Test
    void findDepartmentByIdShouldReturnEntityWithSpecifiedId() {
        Department expectedDepartment = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .build();

        when(departmentDao.findById(expectedDepartment.getId())).thenReturn(Optional.of(expectedDepartment));

        Department actualDepartment = departmentService.findDepartmentById(expectedDepartment.getId());
        assertEquals(expectedDepartment, actualDepartment);

        verify(departmentDao).findById(expectedDepartment.getId());
    }

    @Test
    void findDepartmentByIdShouldThrowEntityNotFoundExceptionIfEntityWithSpecifiedIdIsNotExists() {
        String departmentId = "hello";

        when(departmentDao.findById(departmentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> departmentService.findDepartmentById(departmentId));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findById(departmentId);
    }

    @Test
    void findDepartmentByNameShouldReturnEntityWithSpecifiedId() {
        Department expectedDepartment = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .build();

        when(departmentDao.findByName(expectedDepartment.getName())).thenReturn(Optional.of(expectedDepartment));

        Department actualDepartment = departmentService.findDepartmentByName(expectedDepartment.getName());
        assertEquals(expectedDepartment, actualDepartment);

        verify(departmentDao).findByName(expectedDepartment.getName());
    }

    @Test
    void findDepartmentByNameShouldThrowEntityNotFoundExceptionIfEntityWithSpecifiedIdIsNotExists() {
        String departmentName = "Programming";

        when(departmentDao.findByName(departmentName)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> departmentService.findDepartmentByName(departmentName));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findByName(departmentName);
    }

    @Test
    void showAllDepartmentsShouldReturnAllDepartments() {
        List<Department> departments = new ArrayList<>();

        when(departmentDao.findAll()).thenReturn(departments);

        departments = departmentService.showAllDepartments();

        verify(departmentDao).findAll();
    }

    @Test
    void deleteDepartmentShouldRemoveDepartmentFromDB() {
        Department expectedDepartment = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .withTeachers(new ArrayList<>())
                .build();

        when(departmentDao.findById(expectedDepartment.getId())).thenReturn(Optional.of(expectedDepartment));
        doNothing().when(departmentDao).deleteById(expectedDepartment.getId());

        departmentService.deleteDepartment(expectedDepartment.getId());

        verify(departmentDao).findById(expectedDepartment.getId());
        verify(departmentDao).deleteById(expectedDepartment.getId());
    }

    @Test
    void deleteDepartmentShouldThrowEntityNotFoundExceptionIfThereIsNoEntityWithSpecifiedId() {
        String departmentId = "hello";

        when(departmentDao.findById(departmentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> departmentService.deleteDepartment(departmentId));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findById(departmentId);
        verifyNoMoreInteractions(departmentDao);
    }

    @Test
    void deleteDepartmentShouldThrowEntityIsNotEmptyExceptionIfDepartmentHaveTeachers() {
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .withTeachers(new ArrayList<>())
                .build();
        Teacher teacher = Teacher.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();
        department.addTeacher(teacher);
        String departmentId = department.getId();

        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(department));

        Exception exception = assertThrows(EntityIsNotEmptyException.class, () -> departmentService.deleteDepartment(departmentId));

        String expectedMessage = "Can't delete non-empty entity";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findById(departmentId);
    }

    @Test
    void editDepartmentShouldUpdateDepartmentInDb() {
        Department departmentToEdit = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .withTeachers(new ArrayList<>())
                .build();

        Department editedDepartment = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Information Security")
                .withTeachers(new ArrayList<>())
                .build();

        when(departmentDao.findById(editedDepartment.getId())).thenReturn(Optional.of(departmentToEdit));
        when(departmentDao.findByName(editedDepartment.getName())).thenReturn(Optional.empty());
        doNothing().when(validator).validate(editedDepartment);
        doNothing().when(departmentDao).update(editedDepartment);

        departmentService.editDepartment(editedDepartment);

        verify(departmentDao).findById(editedDepartment.getId());
        verify(departmentDao).findByName(editedDepartment.getName());
        verify(validator).validate(editedDepartment);
        verify(departmentDao).update(editedDepartment);
    }

    @Test
    void editDepartmentShouldThrowEntityNotFoundExceptionIfThereIsNoDepartmentWithSpecifiedId() {
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .withTeachers(new ArrayList<>())
                .build();

        when(departmentDao.findById(department.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> departmentService.editDepartment(department));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findById(department.getId());
        verifyNoMoreInteractions(departmentDao);
        verifyNoInteractions(validator);
    }

    @Test
    void editDepartmentShouldThrowEntityAlreadyExistExceptionIfSpecifiedNameAlreadyExists() {
        Department departmentToEdit = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .withTeachers(new ArrayList<>())
                .build();

        Department editedDepartment = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Information Security")
                .withTeachers(new ArrayList<>())
                .build();

        when(departmentDao.findById(editedDepartment.getId())).thenReturn(Optional.of(departmentToEdit));
        when(departmentDao.findByName(editedDepartment.getName())).thenReturn(Optional.of(departmentToEdit));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> departmentService.editDepartment(editedDepartment));

        String expectedMessage = "Specified name already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findById(editedDepartment.getId());
        verify(departmentDao).findByName(editedDepartment.getName());
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(departmentDao);
    }

    @Test
    void addTeacherToDepartmentShouldAddTeacherToDepartment() {
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .withTeachers(new ArrayList<>())
                .build();
        Teacher teacher = Teacher.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();

        String departmentId = department.getId();
        String teacherId = teacher.getId();

        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(department));
        when(teacherDao.findById(teacherId)).thenReturn(Optional.of(teacher));
        doNothing().when(departmentDao).addTeacherToDepartment(departmentId, teacherId);

        departmentService.addTeacherToDepartment(departmentId, teacherId);

        verify(departmentDao).findById(departmentId);
        verify(teacherDao).findById(teacherId);
        verify(departmentDao).addTeacherToDepartment(departmentId, teacherId);
    }

    @Test
    void addTeacherToDepartmentShouldThrowEntityNoFoundExceptionIfSpecifiedDepartmentNotExists() {
        String departmentId = "hello";
        String teacherId = "world";

        when(departmentDao.findById(departmentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> departmentService.addTeacherToDepartment(departmentId, teacherId));

        String expectedMessage = "Specified department not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findById(departmentId);
        verifyNoMoreInteractions(departmentDao);
        verifyNoInteractions(teacherDao);
    }

    @Test
    void addTeacherToDepartmentShouldThrowEntityNoFoundExceptionIfSpecifiedTeacherNotExists() {
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .withTeachers(new ArrayList<>())
                .build();

        String teacherId = "world";
        String departmentId = department.getId();

        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(department));
        when(teacherDao.findById(teacherId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> departmentService.addTeacherToDepartment(departmentId, teacherId));

        String expectedMessage = "Specified teacher not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findById(departmentId);
        verify(teacherDao).findById(teacherId);
        verifyNoMoreInteractions(departmentDao);
        verifyNoMoreInteractions(teacherDao);
    }

    @Test
    void addTeacherToDepartmentShouldThrowEntityAlreadyExistExceptionIfSpecifiedTeacherAlreadyInDepartment() {
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .withTeachers(new ArrayList<>())
                .build();

        Teacher teacher = Teacher.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();
        department.addTeacher(teacher);

        String departmentId = department.getId();
        String teacherId = teacher.getId();

        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(department));
        when(teacherDao.findById(teacherId)).thenReturn(Optional.of(teacher));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> departmentService.addTeacherToDepartment(departmentId, teacherId));

        String expectedMessage = "Specified teacher already in department";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findById(departmentId);
        verify(teacherDao).findById(teacherId);
        verifyNoMoreInteractions(departmentDao);
        verifyNoMoreInteractions(teacherDao);
    }

    @Test
    void removeTeacherFromDepartmentShouldRemoveTeacherFromDepartment() {
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .withTeachers(new ArrayList<>())
                .build();

        Teacher teacher = Teacher.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();
        department.addTeacher(teacher);

        String departmentId = department.getId();
        String teacherId = teacher.getId();

        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(department));
        when(teacherDao.findById(teacherId)).thenReturn(Optional.of(teacher));
        doNothing().when(departmentDao).removeTeacherFromDepartment(departmentId, teacherId);

        departmentService.removeTeacherFromDepartment(departmentId, teacherId);

        verify(departmentDao).findById(departmentId);
        verify(teacherDao).findById(teacherId);
        verify(departmentDao).removeTeacherFromDepartment(departmentId, teacherId);
    }

    @Test
    void removeTeacherFromDepartmentShouldThrowEntityNoFoundExceptionIfSpecifiedDepartmentNotExists() {
        String departmentId = "hello";
        String teacherId = "world";

        when(departmentDao.findById(departmentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> departmentService.removeTeacherFromDepartment(departmentId, teacherId));

        String expectedMessage = "Specified department not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findById(departmentId);
        verifyNoMoreInteractions(departmentDao);
        verifyNoInteractions(teacherDao);
    }

    @Test
    void removeTeacherFromDepartmentShouldThrowEntityNoFoundExceptionIfSpecifiedTeacherNotExists() {
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .withTeachers(new ArrayList<>())
                .build();

        String teacherId = "world";
        String departmentId = department.getId();

        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(department));
        when(teacherDao.findById(teacherId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> departmentService.removeTeacherFromDepartment(departmentId, teacherId));

        String expectedMessage = "Specified teacher not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findById(departmentId);
        verify(teacherDao).findById(teacherId);
        verifyNoMoreInteractions(departmentDao);
        verifyNoMoreInteractions(teacherDao);
    }

    @Test
    void removeTeacherFromDepartmentShouldThrowEntityAlreadyExistExceptionIfSpecifiedTeacherIsNotInADepartment() {
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Programming")
                .withTeachers(new ArrayList<>())
                .build();

        Teacher teacher = Teacher.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();

        String departmentId = department.getId();
        String teacherId = teacher.getId();

        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(department));
        when(teacherDao.findById(teacherId)).thenReturn(Optional.of(teacher));

        Exception exception = assertThrows(EntityNotFoundException.class, () -> departmentService.removeTeacherFromDepartment(departmentId, teacherId));

        String expectedMessage = "Specified teacher is not in a department";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(departmentDao).findById(departmentId);
        verify(teacherDao).findById(teacherId);
        verifyNoMoreInteractions(departmentDao);
        verifyNoMoreInteractions(teacherDao);
    }
}
