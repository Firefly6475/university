package ua.com.foxminded.university.spring.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.spring.config.JdbcConfigTest;
import ua.com.foxminded.university.spring.dao.DepartmentDao;
import ua.com.foxminded.university.spring.dao.TeacherDao;
import ua.com.foxminded.university.spring.dao.mapper.DepartmentMapper;
import ua.com.foxminded.university.spring.dao.mapper.TeacherMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DepartmentDaoImplTest {
    private final TeacherMapper teacherMapper = new TeacherMapper();
    private final JdbcConfigTest jdbcConfigTest = new JdbcConfigTest();
    private final DepartmentMapper departmentMapper = new DepartmentMapper(teacherMapper);
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(jdbcConfigTest.getTestDataSource());
    private final TeacherDao teacherDao = new TeacherDaoImpl(jdbcTemplate, teacherMapper);
    private final DepartmentDao departmentDao = new DepartmentDaoImpl(jdbcTemplate, departmentMapper);

    @Test
    void saveShouldInsertDepartmentInDB() {
        List<Teacher> teachers = teacherDao.findAll();
        Department department = Department.builder()
                .id(UUID.randomUUID().toString())
                .name("new Department")
                .teachers(teachers)
                .build();
        departmentDao.save(department);
        departmentDao.addAllTeachersToDepartment(department);
        Department actualDepartment = departmentDao.findById(department.getId()).get();

        assertEquals(department, actualDepartment);
    }

    @Test
    void saveAllShouldInsertListOfDepartmentsInDB() {
        List<Teacher> teachers = teacherDao.findAll();
        Department department1 = Department.builder()
                .id(UUID.randomUUID().toString())
                .name("super Department")
                .teachers(teachers)
                .build();
        Department department2 = Department.builder()
                .id(UUID.randomUUID().toString())
                .name("new Department")
                .teachers(teachers)
                .build();
        List<Department> expectedDepartments = new ArrayList<>();
        expectedDepartments.add(department1);
        expectedDepartments.add(department2);

        departmentDao.saveAll(expectedDepartments);
        departmentDao.addAllTeachersToDepartment(department1);
        departmentDao.addAllTeachersToDepartment(department2);

        List<Department> actualDepartments = departmentDao.findAll();

        assertTrue(actualDepartments.containsAll(expectedDepartments));
    }

    @Test
    void updateShouldChangeDepartmentField() {
        Department department = departmentDao.findById("aabb").get();
        Department expectedDepartment = Department.builder()
                .id("aabb")
                .name("super department")
                .teachers(department.getTeachers())
                .build();
        assertNotEquals(department.getName(), expectedDepartment.getName());

        departmentDao.update(expectedDepartment);
        Department actualDepartment = departmentDao.findById("aabb").get();

        assertEquals(expectedDepartment, actualDepartment);
    }

    @Test
    void addTeacherToDepartmentShouldAddTeacherToDepartmentList() {
        Teacher teacher = teacherDao.findById("ffgg").get();

        Department expectedDepartment = departmentDao.findById("aabb").get();
        assertFalse(expectedDepartment.getTeachers().contains(teacher));

        departmentDao.addTeacherToDepartment(expectedDepartment.getId(), teacher.getId());
        expectedDepartment.addTeacher(teacher);
        Department actualDepartment = departmentDao.findById("aabb").get();

        assertEquals(expectedDepartment, actualDepartment);
    }

    @Test
    void deleteTeacherFromDepartmentShouldRemoveTeacherFromDepartment() {
        Department expectedDepartment = departmentDao.findById("aabb").get();
        Teacher teacher = expectedDepartment.getTeachers().get(0);
        expectedDepartment.getTeachers().remove(teacher);
        departmentDao.removeTeacherFromDepartment(expectedDepartment.getId(), teacher.getId());
        Department actualDepartment = departmentDao.findById("aabb").get();

        assertEquals(expectedDepartment, actualDepartment);
    }
}
