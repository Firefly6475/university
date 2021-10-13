package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityIsNotEmptyException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.exception.InvalidNameException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.GroupDao;
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
public class GroupServiceTest {
    @Mock
    private GroupDao groupDao;

    @Mock
    private StudentDao studentDao;

    @Mock
    private Validator<Group> validator;

    @InjectMocks
    @Spy
    private GroupServiceImpl groupService;

    @Test
    void addGroupShouldSaveGroup() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("BI-16")
                .withCourse(3)
                .build();

        when(groupDao.findByName(group.getName())).thenReturn(Optional.empty());
        doNothing().when(validator).validate(group);
        doNothing().when(groupDao).save(group);

        groupService.addGroup(group);

        verify(groupDao).findByName(group.getName());
        verify(validator).validate(group);
        verify(groupDao).save(group);
    }

    @Test
    void addGroupShouldThrowEntityAlreadyExistExceptionIfEntityWithSpecifiedNameAlreadyExists() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("BI-16")
                .withCourse(3)
                .build();

        when(groupDao.findByName(group.getName())).thenReturn(Optional.of(group));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> groupService.addGroup(group));

        String expectedMessage = "Entity with specified name is already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findByName(group.getName());
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(groupDao);
    }

    @Test
    void addGroupShouldThrowInvalidNameExceptionIfEntityHaveWrongNameFormat() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("hello")
                .withCourse(3)
                .build();

        when(groupDao.findByName(group.getName())).thenReturn(Optional.empty());
        doThrow(new InvalidNameException("Group name format invalid")).when(validator).validate(group);

        Exception exception = assertThrows(InvalidNameException.class, () -> groupService.addGroup(group));

        String expectedMessage = "Group name format invalid";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findByName(group.getName());
        verify(validator).validate(group);
        verifyNoMoreInteractions(groupDao);
    }

    @Test
    void findGroupByIdShouldReturnEntityWithSpecifiedId() {
        Group expectedGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("hello")
                .withCourse(3)
                .build();

        when(groupDao.findById(expectedGroup.getId())).thenReturn(Optional.of(expectedGroup));

        Group actualGroup = groupService.findGroupById(expectedGroup.getId());
        assertEquals(expectedGroup, actualGroup);

        verify(groupDao).findById(expectedGroup.getId());
    }

    @Test
    void findGroupByIdShouldThrowEntityNotFoundExceptionIfEntityWithSpecifiedIdIsNotExists() {
        String groupId = "hello";

        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> groupService.findGroupById(groupId));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findById(groupId);
    }

    @Test
    void findGroupByNameShouldReturnEntityWithSpecifiedId() {
        Group expectedGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("hello")
                .withCourse(3)
                .build();

        when(groupDao.findByName(expectedGroup.getName())).thenReturn(Optional.of(expectedGroup));

        Group actualGroup = groupService.findGroupByName(expectedGroup.getName());
        assertEquals(expectedGroup, actualGroup);

        verify(groupDao).findByName(expectedGroup.getName());
    }

    @Test
    void findGroupByNameShouldThrowEntityNotFoundExceptionIfEntityWithSpecifiedIdIsNotExists() {
        String groupName = "hello";

        when(groupDao.findByName(groupName)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> groupService.findGroupByName(groupName));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findByName(groupName);
    }

    @Test
    void showAllGroupsShouldReturnAllGroups() {
        List<Group> groups = new ArrayList<>();

        when(groupDao.findAll()).thenReturn(groups);

        groups = groupService.showAllGroups();

        verify(groupDao).findAll();
    }

    @Test
    void deleteGroupShouldRemoveGroupFromDB() {
        Group expectedGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("hello")
                .withCourse(3)
                .withStudents(new ArrayList<>())
                .build();

        when(groupDao.findById(expectedGroup.getId())).thenReturn(Optional.of(expectedGroup));
        doNothing().when(groupDao).deleteById(expectedGroup.getId());

        groupService.deleteGroup(expectedGroup.getId());

        verify(groupDao).findById(expectedGroup.getId());
        verify(groupDao).deleteById(expectedGroup.getId());
    }

    @Test
    void deleteGroupShouldThrowEntityNotFoundExceptionIfThereIsNoEntityWithSpecifiedId() {
        String groupId = "hello";

        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> groupService.deleteGroup(groupId));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findById(groupId);
        verifyNoMoreInteractions(groupDao);
    }

    @Test
    void deleteGroupShouldThrowEntityIsNotEmptyExceptionIfGroupHaveStudents() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("hello")
                .withCourse(3)
                .withStudents(new ArrayList<>())
                .build();
        Student student = Student.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();
        group.addStudent(student);
        String groupId = group.getId();

        when(groupDao.findById(groupId)).thenReturn(Optional.of(group));

        Exception exception = assertThrows(EntityIsNotEmptyException.class, () -> groupService.deleteGroup(groupId));

        String expectedMessage = "Can't delete non-empty entity";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findById(groupId);
    }

    @Test
    void editGroupShouldUpdateGroupInDbIfNameIsNotChanged() {
        Group groupToEdit = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("PI-16")
                .withCourse(3)
                .withStudents(new ArrayList<>())
                .build();

        Group editedGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("PI-16")
                .withCourse(4)
                .withStudents(new ArrayList<>())
                .build();

        when(groupDao.findById(editedGroup.getId())).thenReturn(Optional.of(groupToEdit));
        when(groupService.isNameChanged(editedGroup, groupToEdit)).thenReturn(false);
        doNothing().when(validator).validate(editedGroup);
        doNothing().when(groupDao).update(editedGroup);

        groupService.editGroup(editedGroup);

        verify(groupDao).findById(editedGroup.getId());
        verify(groupService).isNameChanged(editedGroup, groupToEdit);
        verify(validator).validate(editedGroup);
        verify(groupDao).update(editedGroup);
    }

    @Test
    void editGroupShouldUpdateGroupInDbIfNameIsChanged() {
        Group groupToEdit = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("PI-16")
                .withCourse(3)
                .withStudents(new ArrayList<>())
                .build();

        Group editedGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("BI-16")
                .withCourse(4)
                .withStudents(new ArrayList<>())
                .build();

        when(groupDao.findById(editedGroup.getId())).thenReturn(Optional.of(groupToEdit));
        when(groupService.isNameChanged(editedGroup, groupToEdit)).thenReturn(true);
        when(groupDao.findByName(editedGroup.getName())).thenReturn(Optional.empty());
        doNothing().when(validator).validate(editedGroup);
        doNothing().when(groupDao).update(editedGroup);

        groupService.editGroup(editedGroup);

        verify(groupDao).findById(editedGroup.getId());
        verify(groupService).isNameChanged(editedGroup, groupToEdit);
        verify(groupDao).findByName(editedGroup.getName());
        verify(validator).validate(editedGroup);
        verify(groupDao).update(editedGroup);
    }

    @Test
    void editGroupShouldThrowEntityNotFoundExceptionIfThereIsNoGroupWithSpecifiedId() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("BI-16")
                .withCourse(3)
                .withStudents(new ArrayList<>())
                .build();

        when(groupDao.findById(group.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> groupService.editGroup(group));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findById(group.getId());
        verifyNoMoreInteractions(groupDao);
        verifyNoInteractions(validator);
    }

    @Test
    void editGroupShouldThrowEntityAlreadyExistExceptionIfSpecifiedNameAlreadyExists() {
        Group groupToEdit = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("BI-16")
                .withCourse(3)
                .withStudents(new ArrayList<>())
                .build();

        Group editedGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("BI-16")
                .withCourse(4)
                .withStudents(new ArrayList<>())
                .build();

        when(groupDao.findById(editedGroup.getId())).thenReturn(Optional.of(groupToEdit));
        when(groupService.isNameChanged(editedGroup, groupToEdit)).thenReturn(true);
        when(groupDao.findByName(editedGroup.getName())).thenReturn(Optional.of(groupToEdit));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> groupService.editGroup(editedGroup));

        String expectedMessage = "Specified name already exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findById(editedGroup.getId());
        verify(groupService).isNameChanged(editedGroup, groupToEdit);
        verify(groupDao).findByName(editedGroup.getName());
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(groupDao);
    }

    @Test
    void addStudentToGroupShouldAddStudentToGroup() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("someGroup")
                .withCourse(3)
                .withStudents(new ArrayList<>())
                .build();
        Student student = Student.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();

        String groupId = group.getId();
        String studentId = student.getId();

        when(groupDao.findById(groupId)).thenReturn(Optional.of(group));
        when(studentDao.findById(studentId)).thenReturn(Optional.of(student));
        doNothing().when(groupDao).addStudentToGroup(groupId, studentId);

        groupService.addStudentToGroup(groupId, studentId);

        verify(groupDao).findById(groupId);
        verify(studentDao).findById(studentId);
        verify(groupDao).addStudentToGroup(groupId, studentId);
    }

    @Test
    void addStudentToGroupShouldThrowEntityNoFoundExceptionIfSpecifiedGroupNotExists() {
        String groupId = "hello";
        String studentId = "world";

        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> groupService.addStudentToGroup(groupId, studentId));

        String expectedMessage = "Specified group not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findById(groupId);
        verifyNoMoreInteractions(groupDao);
        verifyNoInteractions(studentDao);
    }

    @Test
    void addStudentToGroupShouldThrowEntityNoFoundExceptionIfSpecifiedStudentNotExists() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("someGroup")
                .withCourse(3)
                .withStudents(new ArrayList<>())
                .build();

        String studentId = "world";
        String groupId = group.getId();

        when(groupDao.findById(groupId)).thenReturn(Optional.of(group));
        when(studentDao.findById(studentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> groupService.addStudentToGroup(groupId, studentId));

        String expectedMessage = "Specified student not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findById(groupId);
        verify(studentDao).findById(studentId);
        verifyNoMoreInteractions(groupDao);
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void addStudentToGroupShouldThrowEntityAlreadyExistExceptionIfSpecifiedStudentAlreadyInGroup() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("someGroup")
                .withCourse(3)
                .withStudents(new ArrayList<>())
                .build();
        Student student = Student.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();
        group.addStudent(student);

        String groupId = group.getId();
        String studentId = student.getId();

        when(groupDao.findById(groupId)).thenReturn(Optional.of(group));
        when(studentDao.findById(studentId)).thenReturn(Optional.of(student));

        Exception exception = assertThrows(EntityAlreadyExistException.class, () -> groupService.addStudentToGroup(groupId, studentId));

        String expectedMessage = "Specified student already in group";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findById(groupId);
        verify(studentDao).findById(studentId);
        verifyNoMoreInteractions(groupDao);
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void removeStudentFromGroupShouldRemoveStudentFromGroup() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("someGroup")
                .withCourse(3)
                .withStudents(new ArrayList<>())
                .build();
        Student student = Student.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();
        group.addStudent(student);

        String groupId = group.getId();
        String studentId = student.getId();

        when(groupDao.findById(groupId)).thenReturn(Optional.of(group));
        when(studentDao.findById(studentId)).thenReturn(Optional.of(student));
        doNothing().when(groupDao).removeStudentFromGroup(groupId, studentId);

        groupService.removeStudentFromGroup(groupId, studentId);

        verify(groupDao).findById(groupId);
        verify(studentDao).findById(studentId);
        verify(groupDao).removeStudentFromGroup(groupId, studentId);
    }

    @Test
    void removeStudentFromGroupShouldThrowEntityNoFoundExceptionIfSpecifiedGroupNotExists() {
        String groupId = "hello";
        String studentId = "world";

        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> groupService.removeStudentFromGroup(groupId, studentId));

        String expectedMessage = "Specified group not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findById(groupId);
        verifyNoMoreInteractions(groupDao);
        verifyNoInteractions(studentDao);
    }

    @Test
    void removeStudentFromGroupShouldThrowEntityNoFoundExceptionIfSpecifiedStudentNotExists() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("someGroup")
                .withCourse(3)
                .withStudents(new ArrayList<>())
                .build();
        String studentId = "world";
        String groupId = group.getId();

        when(groupDao.findById(groupId)).thenReturn(Optional.of(group));
        when(studentDao.findById(studentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> groupService.removeStudentFromGroup(groupId, studentId));

        String expectedMessage = "Specified student not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findById(groupId);
        verify(studentDao).findById(studentId);
        verifyNoMoreInteractions(groupDao);
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void removeStudentFromGroupShouldThrowEntityAlreadyExistExceptionIfSpecifiedStudentIsNotInAGroup() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("someGroup")
                .withCourse(3)
                .withStudents(new ArrayList<>())
                .build();
        Student student = Student.builder()
                .withId("hello")
                .withEmail("world@gmail.com")
                .withPassword("12345")
                .withName("Alexey")
                .withBirthday(LocalDate.now())
                .build();

        String groupId = group.getId();
        String studentId = student.getId();

        when(groupDao.findById(groupId)).thenReturn(Optional.of(group));
        when(studentDao.findById(studentId)).thenReturn(Optional.of(student));

        Exception exception = assertThrows(EntityNotFoundException.class, () -> groupService.removeStudentFromGroup(groupId, studentId));

        String expectedMessage = "Specified student is not in a group";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findById(groupId);
        verify(studentDao).findById(studentId);
        verifyNoMoreInteractions(groupDao);
        verifyNoMoreInteractions(studentDao);
    }
}
