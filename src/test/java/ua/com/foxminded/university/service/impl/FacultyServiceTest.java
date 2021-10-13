package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityIsNotEmptyException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.exception.InvalidNameException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.FacultyDao;
import ua.com.foxminded.university.spring.dao.GroupDao;
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
public class FacultyServiceTest {
    @Mock
    private FacultyDao facultyDao;

    @Mock
    private Validator<Faculty> validator;

    @Mock
    private GroupDao groupDao;

    @InjectMocks
    private FacultyServiceImpl facultyService;

    @Test
    void addFacultyShouldSaveFaculty() {
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .build();

        when(facultyDao.findByName(faculty.getName())).thenReturn(Optional.empty());
        doNothing().when(validator).validate(faculty);
        doNothing().when(facultyDao).save(faculty);

        facultyService.addFaculty(faculty);

        verify(facultyDao).findByName(faculty.getName());
        verify(validator).validate(faculty);
        verify(facultyDao).save(faculty);
    }

    @Test
    void addFacultyShouldThrowEntityAlreadyExistExceptionIfEntityWithSpecifiedNameAlreadyExists() {
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .build();

        when(facultyDao.findByName(faculty.getName())).thenReturn(Optional.of(faculty));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> facultyService.addFaculty(faculty));

        String expectedMessage = "Entity with specified name is already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findByName(faculty.getName());
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(facultyDao);
    }

    @Test
    void addFacultyShouldThrowInvalidNameExceptionIfEntityHaveNameWithLessThan4Symbols() {
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("hi")
                .build();

        when(facultyDao.findByName(faculty.getName())).thenReturn(Optional.empty());
        doThrow(new InvalidNameException("Faculty name is less than 4 or more than 30 symbols")).when(validator).validate(faculty);

        Exception exception = assertThrows(InvalidNameException.class, () -> facultyService.addFaculty(faculty));

        String expectedMessage = "Faculty name is less than 4 or more than 30 symbols";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findByName(faculty.getName());
        verify(validator).validate(faculty);
        verifyNoMoreInteractions(facultyDao);
    }

    @Test
    void findFacultyByIdShouldReturnEntityWithSpecifiedId() {
        Faculty expectedFaculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .build();

        when(facultyDao.findById(expectedFaculty.getId())).thenReturn(Optional.of(expectedFaculty));

        Faculty actualFaculty = facultyService.findFacultyById(expectedFaculty.getId());
        assertEquals(expectedFaculty, actualFaculty);

        verify(facultyDao).findById(expectedFaculty.getId());
    }

    @Test
    void findFacultyByIdShouldThrowEntityNotFoundExceptionIfEntityWithSpecifiedIdIsNotExists() {
        String facultyId = "hello";

        when(facultyDao.findById(facultyId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> facultyService.findFacultyById(facultyId));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findById(facultyId);
    }

    @Test
    void findFacultyByNameShouldReturnEntityWithSpecifiedId() {
        Faculty expectedFaculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .build();

        when(facultyDao.findByName(expectedFaculty.getName())).thenReturn(Optional.of(expectedFaculty));

        Faculty actualFaculty = facultyService.findFacultyByName(expectedFaculty.getName());
        assertEquals(expectedFaculty, actualFaculty);

        verify(facultyDao).findByName(expectedFaculty.getName());
    }

    @Test
    void findFacultyByNameShouldThrowEntityNotFoundExceptionIfEntityWithSpecifiedIdIsNotExists() {
        String facultyName = "Enterprise development";

        when(facultyDao.findByName(facultyName)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> facultyService.findFacultyByName(facultyName));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findByName(facultyName);
    }

    @Test
    void showAllFacultiesShouldReturnAllFaculties() {
        List<Faculty> faculties = new ArrayList<>();

        when(facultyDao.findAll()).thenReturn(faculties);

        faculties = facultyService.showAllFaculties();

        verify(facultyDao).findAll();
    }

    @Test
    void deleteFacultyShouldRemoveFacultyFromDB() {
        Faculty expectedFaculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .withGroups(new ArrayList<>())
                .build();

        when(facultyDao.findById(expectedFaculty.getId())).thenReturn(Optional.of(expectedFaculty));
        doNothing().when(facultyDao).deleteById(expectedFaculty.getId());

        facultyService.deleteFaculty(expectedFaculty.getId());

        verify(facultyDao).findById(expectedFaculty.getId());
        verify(facultyDao).deleteById(expectedFaculty.getId());
    }

    @Test
    void deleteFacultyShouldThrowEntityNotFoundExceptionIfThereIsNoEntityWithSpecifiedId() {
        String facultyId = "hello";

        when(facultyDao.findById(facultyId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> facultyService.deleteFaculty(facultyId));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findById(facultyId);
        verifyNoMoreInteractions(facultyDao);
    }

    @Test
    void deleteFacultyShouldThrowEntityIsNotEmptyExceptionIfFacultyHaveGroups() {
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .withGroups(new ArrayList<>())
                .build();

        Group group = Group.builder()
                .withId("hello")
                .withName("BI-15")
                .build();

        faculty.addGroup(group);
        String facultyId = faculty.getId();

        when(facultyDao.findById(facultyId)).thenReturn(Optional.of(faculty));

        Exception exception = assertThrows(EntityIsNotEmptyException.class, () -> facultyService.deleteFaculty(facultyId));

        String expectedMessage = "Can't delete non-empty entity";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findById(facultyId);
    }

    @Test
    void editFacultyShouldUpdateFacultyInDb() {
        Faculty facultyToEdit = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .withGroups(new ArrayList<>())
                .build();

        Faculty editedFaculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Mobile development")
                .withGroups(new ArrayList<>())
                .build();

        when(facultyDao.findById(editedFaculty.getId())).thenReturn(Optional.of(facultyToEdit));
        when(facultyDao.findByName(editedFaculty.getName())).thenReturn(Optional.empty());
        doNothing().when(validator).validate(editedFaculty);
        doNothing().when(facultyDao).update(editedFaculty);

        facultyService.editFaculty(editedFaculty);

        verify(facultyDao).findById(editedFaculty.getId());
        verify(facultyDao).findByName(editedFaculty.getName());
        verify(validator).validate(editedFaculty);
        verify(facultyDao).update(editedFaculty);
    }

    @Test
    void editFacultyShouldThrowEntityNotFoundExceptionIfThereIsNoFacultyWithSpecifiedId() {
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .withGroups(new ArrayList<>())
                .build();

        when(facultyDao.findById(faculty.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> facultyService.editFaculty(faculty));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findById(faculty.getId());
        verifyNoMoreInteractions(facultyDao);
        verifyNoInteractions(validator);
    }

    @Test
    void editFacultyShouldThrowEntityAlreadyExistExceptionIfSpecifiedNameAlreadyExists() {
        Faculty facultyToEdit = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .withGroups(new ArrayList<>())
                .build();

        Faculty editedFaculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Mobile development")
                .withGroups(new ArrayList<>())
                .build();

        when(facultyDao.findById(editedFaculty.getId())).thenReturn(Optional.of(facultyToEdit));
        when(facultyDao.findByName(editedFaculty.getName())).thenReturn(Optional.of(facultyToEdit));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> facultyService.editFaculty(editedFaculty));

        String expectedMessage = "Specified name already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findById(editedFaculty.getId());
        verify(facultyDao).findByName(editedFaculty.getName());
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(facultyDao);
    }

    @Test
    void addGroupToFacultyShouldAddGroupToFaculty() {
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .withGroups(new ArrayList<>())
                .build();

        Group group = Group.builder()
                .withId("hello")
                .withName("BI-15")
                .build();

        String facultyId = faculty.getId();
        String groupId = group.getId();

        when(facultyDao.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(groupDao.findById(groupId)).thenReturn(Optional.of(group));
        doNothing().when(facultyDao).addGroupToFaculty(facultyId, groupId);

        facultyService.addGroupToFaculty(facultyId, groupId);

        verify(facultyDao).findById(facultyId);
        verify(groupDao).findById(groupId);
        verify(facultyDao).addGroupToFaculty(facultyId, groupId);
    }

    @Test
    void addGroupToFacultyShouldThrowEntityNoFoundExceptionIfSpecifiedFacultyNotExists() {
        String facultyId = "hello";
        String groupId = "world";

        when(facultyDao.findById(facultyId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> facultyService.addGroupToFaculty(facultyId, groupId));

        String expectedMessage = "Specified faculty not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findById(facultyId);
        verifyNoMoreInteractions(facultyDao);
        verifyNoInteractions(groupDao);
    }

    @Test
    void addGroupToFacultyShouldThrowEntityNoFoundExceptionIfSpecifiedGroupNotExists() {
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .withGroups(new ArrayList<>())
                .build();

        String groupId = "world";
        String facultyId = faculty.getId();

        when(facultyDao.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> facultyService.addGroupToFaculty(facultyId, groupId));

        String expectedMessage = "Specified group not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findById(facultyId);
        verify(groupDao).findById(groupId);
        verifyNoMoreInteractions(facultyDao);
        verifyNoMoreInteractions(groupDao);
    }

    @Test
    void addGroupToFacultyShouldThrowEntityAlreadyExistExceptionIfSpecifiedGroupAlreadyInFaculty() {
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .withGroups(new ArrayList<>())
                .build();

        Group group = Group.builder()
                .withId("hello")
                .withName("BI-15")
                .build();

        faculty.addGroup(group);

        String facultyId = faculty.getId();
        String groupId = group.getId();

        when(facultyDao.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(groupDao.findById(groupId)).thenReturn(Optional.of(group));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> facultyService.addGroupToFaculty(facultyId, groupId));

        String expectedMessage = "Specified group already in faculty";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findById(facultyId);
        verify(groupDao).findById(groupId);
        verifyNoMoreInteractions(facultyDao);
        verifyNoMoreInteractions(groupDao);
    }

    @Test
    void removeGroupFromFacultyShouldRemoveGroupFromFaculty() {
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .withGroups(new ArrayList<>())
                .build();

        Group group = Group.builder()
                .withId("hello")
                .withName("BI-15")
                .build();

        faculty.addGroup(group);

        String facultyId = faculty.getId();
        String groupId = group.getId();

        when(facultyDao.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(groupDao.findById(groupId)).thenReturn(Optional.of(group));
        doNothing().when(facultyDao).removeGroupFromFaculty(facultyId, groupId);

        facultyService.removeGroupFromFaculty(facultyId, groupId);

        verify(facultyDao).findById(facultyId);
        verify(groupDao).findById(groupId);
        verify(facultyDao).removeGroupFromFaculty(facultyId, groupId);
    }

    @Test
    void removeGroupFromFacultyShouldThrowEntityNoFoundExceptionIfSpecifiedFacultyNotExists() {
        String facultyId = "hello";
        String groupId = "world";

        when(facultyDao.findById(facultyId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> facultyService.removeGroupFromFaculty(facultyId, groupId));

        String expectedMessage = "Specified faculty not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findById(facultyId);
        verifyNoMoreInteractions(facultyDao);
        verifyNoInteractions(groupDao);
    }

    @Test
    void removeGroupFromFacultyShouldThrowEntityNoFoundExceptionIfSpecifiedGroupNotExists() {
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .withGroups(new ArrayList<>())
                .build();

        String groupId = "world";
        String facultyId = faculty.getId();

        when(facultyDao.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> facultyService.removeGroupFromFaculty(facultyId, groupId));

        String expectedMessage = "Specified group not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findById(facultyId);
        verify(groupDao).findById(groupId);
        verifyNoMoreInteractions(facultyDao);
        verifyNoMoreInteractions(groupDao);
    }

    @Test
    void removeGroupFromFacultyShouldThrowEntityAlreadyExistExceptionIfSpecifiedGroupIsNotInAFaculty() {
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Enterprise development")
                .withGroups(new ArrayList<>())
                .build();

        Group group = Group.builder()
                .withId("hello")
                .withName("BI-15")
                .build();

        String facultyId = faculty.getId();
        String groupId = group.getId();

        when(facultyDao.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(groupDao.findById(groupId)).thenReturn(Optional.of(group));

        Exception exception = assertThrows(EntityNotFoundException.class, () -> facultyService.removeGroupFromFaculty(facultyId, groupId));

        String expectedMessage = "Specified group is not in a faculty";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(facultyDao).findById(facultyId);
        verify(groupDao).findById(groupId);
        verifyNoMoreInteractions(facultyDao);
        verifyNoMoreInteractions(groupDao);
    }
}
