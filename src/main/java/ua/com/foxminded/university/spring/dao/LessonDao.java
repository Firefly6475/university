package ua.com.foxminded.university.spring.dao;

import ua.com.foxminded.university.model.Lesson;

import java.util.List;

public interface LessonDao extends CrudDao<Lesson> {
    List<Lesson> findByDisciplineName(String disciplineName);
}
