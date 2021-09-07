package ua.com.foxminded.university.spring.dao;

import ua.com.foxminded.university.model.Teacher;

import java.util.Optional;

public interface TeacherDao extends CrudDao<Teacher> {
    Optional<Teacher> findByEmail(String email);
}
