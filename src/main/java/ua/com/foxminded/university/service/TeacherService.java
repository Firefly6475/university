package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Teacher;

import java.util.List;

public interface TeacherService {
    void registerTeacher(Teacher teacher);

    Teacher findTeacherById(String id);

    Teacher findTeacherByEmail(String email);

    List<Teacher> showAllTeachers();

    List<Teacher> showAllTeachers(Integer pageNumber);

    void deleteTeacher(String id);

    void editTeacher(Teacher teacher);
}
