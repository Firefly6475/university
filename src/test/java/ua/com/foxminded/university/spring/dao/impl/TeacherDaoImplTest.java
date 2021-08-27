package ua.com.foxminded.university.spring.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.spring.dao.mapper.TeacherMapper;
import ua.com.foxminded.university.spring.config.JdbcConfigTest;
import ua.com.foxminded.university.spring.dao.Page;
import ua.com.foxminded.university.spring.dao.TeacherDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TeacherDaoImplTest {
    private final JdbcConfigTest jdbcConfigTest = new JdbcConfigTest();
    private final TeacherMapper teacherMapper = new TeacherMapper();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(jdbcConfigTest.getTestDataSource());
    private final TeacherDao teacherDao = new TeacherDaoImpl(jdbcTemplate, teacherMapper);

    @Test
    void saveShouldInsertTeacherInDB() {
        Teacher expectedTeacher = Teacher.builder()
                .id(UUID.randomUUID().toString())
                .name("Alexey")
                .salary(1000)
                .birthday(LocalDate.parse("1998-01-01"))
                .build();
        teacherDao.save(expectedTeacher);
        Teacher actualTeacher = teacherDao.findById(expectedTeacher.getId()).get();
        assertEquals(expectedTeacher, actualTeacher);
    }

    @Test
    void saveAllShouldInsertListOfTeachersInDB() {
        Teacher teacher1 = Teacher.builder()
                .id(UUID.randomUUID().toString())
                .name("Alexey")
                .salary(200)
                .birthday(LocalDate.parse("1998-01-01"))
                .build();
        Teacher teacher2 = Teacher.builder()
                .id(UUID.randomUUID().toString())
                .name("Ivan")
                .salary(500)
                .birthday(LocalDate.parse("1999-01-01"))
                .build();
        List<Teacher> expectedTeachers = new ArrayList<>();
        expectedTeachers.add(teacher1);
        expectedTeachers.add(teacher2);
        teacherDao.saveAll(expectedTeachers);
        List<Teacher> actualTeachers = teacherDao.findAll();

        assertTrue(actualTeachers.containsAll(expectedTeachers));
    }

    @Test
    void updateShouldChangeTeacherField() {
        Teacher teacher = teacherDao.findById("aabb").get();
        Teacher expectedTeacher = Teacher.builder()
                .id("aabb")
                .name("Konstantine")
                .birthday(LocalDate.parse("2010-01-01"))
                .salary(500)
                .build();
        assertFalse(teacher.getName().equals(expectedTeacher.getName()));

        teacherDao.update(expectedTeacher);
        Teacher actualTeacher = teacherDao.findById("aabb").get();

        assertEquals(expectedTeacher, actualTeacher);
    }

    @Test
    void findAllPagedShouldFindSecondPageWith2Results() {
        List<Teacher> teachers = teacherDao.findAll();
        List<Teacher> expectedTeachers = new ArrayList<>();
        expectedTeachers.add(teachers.get(2));
        expectedTeachers.add(teachers.get(3));

        Page page = new Page(2, 2);
        List<Teacher> actualTeachers = teacherDao.findAll(page);

        assertEquals(expectedTeachers, actualTeachers);
    }

    @Test
    void deleteByIdShouldDeleteTeacherWithSpecifiedId() {
        Optional<Teacher> Teacher = teacherDao.findById("ffgg");
        assertTrue(Teacher.isPresent());
        teacherDao.deleteById("ffgg");

        assertFalse(teacherDao.findById("ffgg").isPresent());
    }
}
