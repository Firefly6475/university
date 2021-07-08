package ua.com.foxminded.university.spring.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.spring.dao.mapper.StudentMapper;
import ua.com.foxminded.university.spring.config.JdbcConfigTest;
import ua.com.foxminded.university.spring.dao.Page;
import ua.com.foxminded.university.spring.dao.StudentDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StudentDaoImplTest {
    private final JdbcConfigTest jdbcConfigTest = new JdbcConfigTest();
    private final StudentMapper studentMapper = new StudentMapper();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(jdbcConfigTest.getTestDataSource());
    private final StudentDao studentDao = new StudentDaoImpl(jdbcTemplate, studentMapper);

    @Test
    void saveShouldInsertStudentInDB() {
        Student expectedStudent = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Alexey")
                .withCourse(2)
                .withBirthday(LocalDate.parse("1998-01-01"))
                .build();
        studentDao.save(expectedStudent);
        Student actualStudent = studentDao.findById(expectedStudent.getId()).get();

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void saveAllShouldInsertListOfStudentsInDB() {
        Student student1 = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Alexey")
                .withCourse(2)
                .withBirthday(LocalDate.parse("1998-01-01"))
                .build();
        Student student2 = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Ivan")
                .withCourse(2)
                .withBirthday(LocalDate.parse("1999-01-01"))
                .build();
        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(student1);
        expectedStudents.add(student2);
        studentDao.saveAll(expectedStudents);
        List<Student> actualStudents = studentDao.findAll();

        assertTrue(actualStudents.containsAll(expectedStudents));
    }

    @Test
    void updateShouldChangeStudentField() {
        Student student = studentDao.findById("aabb").get();
        Student expectedStudent = Student.builder()
                .withName("Konstantine")
                .withId("aabb")
                .withBirthday(LocalDate.parse("2010-01-01"))
                .withCourse(2)
                .build();
        assertFalse(student.getName().equals(expectedStudent.getName()));

        studentDao.update(expectedStudent);
        Student actualStudent = studentDao.findById("aabb").get();

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void findAllPagedShouldFindSecondPageWith2Results() {
        List<Student> students = studentDao.findAll();
        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(students.get(2));
        expectedStudents.add(students.get(3));

        Page page = new Page(2, 2);
        List<Student> actualStudents = studentDao.findAll(page);

        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    void deleteByIdShouldDeleteStudentWithSpecifiedId() {
        Optional<Student> student = studentDao.findById("ffgg");
        assertTrue(student.isPresent());
        studentDao.deleteById("ffgg");

        assertFalse(studentDao.findById("ffgg").isPresent());
    }
}
