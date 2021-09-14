package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityIsNotEmptyException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.exception.InvalidNameException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.GroupDao;
import ua.com.foxminded.university.spring.dao.Page;
import ua.com.foxminded.university.spring.dao.impl.GroupDaoImpl;
import ua.com.foxminded.university.spring.dao.mapper.GroupMapper;
import ua.com.foxminded.university.spring.dao.mapper.StudentMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    void showAllGroupsShouldReturnFirstPageWith2Entities() {
        List<Group> groups = new ArrayList<>();
        Page page = new Page(1, 2);

        when(groupDao.findAll(page)).thenReturn(groups);

        groups = groupService.showAllGroups(page);

        verify(groupDao).findAll(page);
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

        when(groupDao.findById(group.getId())).thenReturn(Optional.of(group));

        Exception exception = assertThrows(EntityIsNotEmptyException.class, () -> groupService.deleteGroup(group.getId()));

        String expectedMessage = "Can't delete non-empty entity";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(groupDao).findById(group.getId());
    }
}
