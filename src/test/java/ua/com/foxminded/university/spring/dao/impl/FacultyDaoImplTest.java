package ua.com.foxminded.university.spring.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.spring.config.JdbcConfigTest;
import ua.com.foxminded.university.spring.dao.FacultyDao;
import ua.com.foxminded.university.spring.dao.GroupDao;
import ua.com.foxminded.university.spring.dao.mapper.FacultyMapper;
import ua.com.foxminded.university.spring.dao.mapper.GroupMapper;
import ua.com.foxminded.university.spring.dao.mapper.StudentMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FacultyDaoImplTest {
    private final StudentMapper studentMapper = new StudentMapper();
    private final GroupMapper groupMapper = new GroupMapper(studentMapper);
    private final FacultyMapper facultyMapper = new FacultyMapper(groupMapper, studentMapper);
    private final JdbcConfigTest jdbcConfigTest = new JdbcConfigTest();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(jdbcConfigTest.getTestDataSource());
    private final GroupDao groupDao = new GroupDaoImpl(jdbcTemplate, groupMapper);
    private final FacultyDao facultyDao = new FacultyDaoImpl(jdbcTemplate, facultyMapper);

    @Test
    void saveShouldInsertFacultyInDB() {
        List<Group> group = groupDao.findAll();
        Faculty expectedFaculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("new faculty")
                .withGroups(group)
                .build();
        facultyDao.save(expectedFaculty);
        facultyDao.addAllGroupsToFaculty(expectedFaculty);
        Faculty actualFaculty = facultyDao.findById(expectedFaculty.getId()).get();

        assertEquals(expectedFaculty, actualFaculty);
    }

    @Test
    void saveAllShouldInsertListOfFacultiesInDB() {
        List<Group> groups = groupDao.findAll();
        Faculty faculty1 = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("new faculty")
                .withGroups(groups)
                .build();
        Faculty faculty2 = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .withName("super faculty")
                .withGroups(groups)
                .build();
        List<Faculty> expectedFaculties = new ArrayList<>();
        expectedFaculties.add(faculty2);
        expectedFaculties.add(faculty1);
        facultyDao.saveAll(expectedFaculties);
        facultyDao.addAllGroupsToFaculty(faculty1);
        facultyDao.addAllGroupsToFaculty(faculty2);
        List<Faculty> actualFaculties = facultyDao.findAll();

        assertTrue(actualFaculties.containsAll(expectedFaculties));
    }

    @Test
    void updateShouldChangeFacultyField() {
        Group group = groupDao.findById("bbcc").get();
        Faculty faculty = facultyDao.findById("bbcc").get();
        List<Group> groups = new ArrayList<>();
        groups.add(group);

        Faculty expectedFaculty = Faculty.builder()
                .withId("bbcc")
                .withName("super faculty")
                .withGroups(groups)
                .build();
        assertNotEquals(expectedFaculty.getName(), faculty.getName());

        facultyDao.update(expectedFaculty);
        Faculty actualFaculty = facultyDao.findById("bbcc").get();

        assertEquals(expectedFaculty, actualFaculty);
    }

    @Test
    void addGroupToFacultyShouldAddGroupToFacultyList() {
        Group group = groupDao.findById("ccdd").get();

        Faculty expectedFaculty = facultyDao.findById("bbcc").get();
        assertFalse(expectedFaculty.getGroups().contains(group));

        facultyDao.addGroupToFaculty(expectedFaculty.getId(), group.getId());
        expectedFaculty.addGroup(group);
        Faculty actualFaculty = facultyDao.findById("bbcc").get();

        assertEquals(expectedFaculty, actualFaculty);
    }

    @Test
    void deleteGroupFromFacultyShouldRemoveGroupFromFacultyInDB() {
        Faculty expectedFaculty = facultyDao.findById("aabb").get();
        Group group = expectedFaculty.getGroups().get(0);
        expectedFaculty.getGroups().remove(group);
        facultyDao.removeGroupFromFaculty(expectedFaculty.getId(), group.getId());
        Faculty actualFaculty = facultyDao.findById("aabb").get();

        assertEquals(expectedFaculty, actualFaculty);
    }
}
