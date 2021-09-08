package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.spring.dao.Page;

import java.util.List;

public interface StudentService {
    void registerStudent(Student student);

    Student findStudentById(String id);

    Student findStudentByEmail(String email);

    List<Student> showAllStudents(Page page);

    void deleteStudent(String id);

    void editStudent(Student student);
}
