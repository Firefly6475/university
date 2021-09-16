package ua.com.foxminded.university.spring.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.spring.config.JdbcConfigTest;
import ua.com.foxminded.university.spring.dao.DisciplineDao;
import ua.com.foxminded.university.spring.dao.TeacherDao;
import ua.com.foxminded.university.spring.dao.mapper.DisciplineMapper;
import ua.com.foxminded.university.spring.dao.mapper.TeacherMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DisciplineDaoImplTest {
    private final TeacherMapper teacherMapper = new TeacherMapper();
    private final JdbcConfigTest jdbcConfigTest = new JdbcConfigTest();
    private final DisciplineMapper disciplineMapper = new DisciplineMapper(teacherMapper);
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(jdbcConfigTest.getTestDataSource());
    private final TeacherDao teacherDao = new TeacherDaoImpl(jdbcTemplate, teacherMapper);
    private final DisciplineDao disciplineDao = new DisciplineDaoImpl(jdbcTemplate, disciplineMapper);

    @Test
    void saveShouldInsertDisciplineInDB() {
        List<Teacher> teachers = teacherDao.findAll();
        Discipline expectedDiscipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("new Discipline")
                .withTeachers(teachers)
                .build();
        disciplineDao.save(expectedDiscipline);
        disciplineDao.addAllTeachersToDiscipline(expectedDiscipline);
        Discipline actualDiscipline = disciplineDao.findById(expectedDiscipline.getId()).get();

        assertEquals(expectedDiscipline, actualDiscipline);
    }

    @Test
    void saveAllShouldInsertListOfDisciplinesInDB() {
        List<Teacher> teachers = teacherDao.findAll();
        Discipline discipline1 = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("super Discipline")
                .withTeachers(teachers)
                .build();
        Discipline discipline2 = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("new Discipline")
                .withTeachers(teachers)
                .build();
        List<Discipline> expectedDisciplines = new ArrayList<>();
        expectedDisciplines.add(discipline1);
        expectedDisciplines.add(discipline2);

        disciplineDao.saveAll(expectedDisciplines);
        disciplineDao.addAllTeachersToDiscipline(discipline1);
        disciplineDao.addAllTeachersToDiscipline(discipline2);

        List<Discipline> actualDisciplines = disciplineDao.findAll();

        assertTrue(actualDisciplines.containsAll(expectedDisciplines));
    }

    @Test
    void updateShouldChangeDisciplineField() {
        Discipline discipline = disciplineDao.findById("aabb").get();
        Discipline expectedDiscipline = Discipline.builder()
                .withId("aabb")
                .withName("super discipline")
                .withTeachers(discipline.getTeachers())
                .build();
        assertNotEquals(discipline.getName(), expectedDiscipline.getName());

        disciplineDao.update(expectedDiscipline);
        Discipline actualDiscipline = disciplineDao.findById("aabb").get();

        assertEquals(expectedDiscipline, actualDiscipline);
    }

    @Test
    void addTeacherToDisciplineShouldAddTeacherToDisciplineList() {
        Teacher teacher = teacherDao.findById("ffgg").get();

        Discipline expectedDiscipline = disciplineDao.findById("aabb").get();
        assertFalse(expectedDiscipline.getTeachers().contains(teacher));

        disciplineDao.addTeacherToDiscipline(expectedDiscipline.getId(), teacher.getId());
        expectedDiscipline.addTeacher(teacher);
        Discipline actualDiscipline = disciplineDao.findById("aabb").get();

        assertEquals(expectedDiscipline, actualDiscipline);
    }

    @Test
    void deleteTeacherFromDisciplineShouldRemoveTeacherFromDiscipline() {
        Discipline expectedDiscipline = disciplineDao.findById("aabb").get();
        Teacher teacher = expectedDiscipline.getTeachers().get(0);
        expectedDiscipline.getTeachers().remove(teacher);
        disciplineDao.removeTeacherFromDiscipline(expectedDiscipline.getId(), teacher.getId());
        Discipline actualDiscipline = disciplineDao.findById("aabb").get();

        assertEquals(expectedDiscipline, actualDiscipline);
    }

    @Test
    void findByIdShouldReturnDisciplineIfThereIsNoTeachersInDiscipline() {
        Discipline expectedDiscipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("new Discipline")
                .withTeachers(new ArrayList<>())
                .build();
        disciplineDao.save(expectedDiscipline);

        Discipline actualDiscipline = disciplineDao.findById(expectedDiscipline.getId()).get();

        assertEquals(expectedDiscipline, actualDiscipline);
    }

    @Test
    void findByNameShouldReturnDisciplineWithSpecifiedName() {
        Discipline expectedDiscipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Java")
                .withTeachers(new ArrayList<>())
                .build();

        disciplineDao.save(expectedDiscipline);

        Discipline actualDiscipline = disciplineDao.findByName(expectedDiscipline.getName()).get();
        assertEquals(expectedDiscipline, actualDiscipline);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfNoDisciplineWithSpecifiedName() {
        String disciplineName = "Java";

        Optional<Discipline> expectedDiscipline = Optional.empty();
        Optional<Discipline> actualDiscipline = disciplineDao.findByName(disciplineName);

        assertEquals(expectedDiscipline, actualDiscipline);
    }
}
