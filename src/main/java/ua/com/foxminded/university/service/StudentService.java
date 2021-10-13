package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Student;

import java.util.List;

public interface StudentService {
    void registerStudent(Student student);

    Student findStudentById(String id);

    Student findStudentByEmail(String email);

    List<Student> showAllStudents();

    List<Student> showAllStudents(Integer pageNumber);

    void deleteStudent(String id);

    void editStudent(Student student);
}
