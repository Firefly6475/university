package ua.com.foxminded.university.spring.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.spring.config.JdbcConfigTest;
import ua.com.foxminded.university.spring.dao.GroupDao;
import ua.com.foxminded.university.spring.dao.StudentDao;
import ua.com.foxminded.university.spring.dao.mapper.GroupMapper;
import ua.com.foxminded.university.spring.dao.mapper.StudentMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupDaoImplTest {
    private final JdbcConfigTest jdbcConfigTest = new JdbcConfigTest();
    private final StudentMapper studentMapper = new StudentMapper();
    private final GroupMapper groupMapper = new GroupMapper(studentMapper);
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(jdbcConfigTest.getTestDataSource());
    private final GroupDao groupDao = new GroupDaoImpl(jdbcTemplate, groupMapper);
    private final StudentDao studentDao = new StudentDaoImpl(jdbcTemplate, studentMapper);

    @Test
    void saveShouldInsertGroupInDB() {
        List<Student> students = studentDao.findAll();
        Group expectedGroup = Group.builder()
                .id(UUID.randomUUID().toString())
                .name("new Group")
                .students(students)
                .build();
        groupDao.save(expectedGroup);
        groupDao.addAllStudentsToGroup(expectedGroup);
        Group actualGroup = groupDao.findById(expectedGroup.getId()).get();

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void saveAllShouldInsertListOfGroupsInDB() {
        List<Student> students = studentDao.findAll();
        Group group1 = Group.builder()
                .id(UUID.randomUUID().toString())
                .name("super Group")
                .students(students)
                .build();
        Group group2 = Group.builder()
                .id(UUID.randomUUID().toString())
                .name("new Group")
                .students(students)
                .build();
        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(group1);
        expectedGroups.add(group2);

        groupDao.saveAll(expectedGroups);
        groupDao.addAllStudentsToGroup(group1);
        groupDao.addAllStudentsToGroup(group2);

        List<Group> actualGroups = groupDao.findAll();

        assertTrue(actualGroups.containsAll(expectedGroups));
    }

    @Test
    void updateShouldChangeGroupField() {
        Group group = groupDao.findById("aabb").get();
        Group expectedGroup = Group.builder()
                .id("aabb")
                .name("super group")
                .students(group.getStudents())
                .build();
        assertNotEquals(group.getName(), expectedGroup.getName());

        groupDao.update(expectedGroup);
        Group actualGroup = groupDao.findById("aabb").get();

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void addStudentToGroupShouldAddStudentToGroupList() {
        Student student = studentDao.findById("ffgg").get();

        Group expectedGroup = groupDao.findById("aabb").get();
        assertFalse(expectedGroup.getStudents().contains(student));

        groupDao.addStudentToGroup(expectedGroup.getId(), student.getId());
        expectedGroup.addStudent(student);
        Group actualGroup = groupDao.findById("aabb").get();

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void deleteStudentFromGroupShouldRemoveStudentFromGroup() {
        Group expectedGroup = groupDao.findById("aabb").get();
        Student student = expectedGroup.getStudents().get(0);
        expectedGroup.getStudents().remove(student);
        groupDao.removeStudentFromGroup(expectedGroup.getId(), student.getId());
        Group actualGroup = groupDao.findById("aabb").get();

        assertEquals(expectedGroup, actualGroup);
    }
}
