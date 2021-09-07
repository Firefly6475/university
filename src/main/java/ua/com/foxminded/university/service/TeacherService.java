package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.spring.dao.Page;

import java.util.List;

public interface TeacherService {
    void registerTeacher(Teacher teacher);

    Teacher findTeacherById(String id);

    Teacher findTeacherByEmail(String email);

    List<Teacher> showAllTeachers(Page page);

    void deleteTeacher(String id);

    void editTeacher(Teacher teacher);
}
