package ua.com.foxminded.university.spring.dao;

import ua.com.foxminded.university.model.Student;

import java.util.Optional;

public interface StudentDao extends CrudDao<Student> {
    Optional<Student> findByEmail(String email);
}
