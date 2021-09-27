package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.spring.dao.Page;

import java.util.List;

public interface LessonService {
    void addLesson(Lesson lesson);

    Lesson findLessonById(String id);

    List<Lesson> findLessonsByDisciplineName(String disciplineName);

    List<Lesson> showAllLessons(Page page);

    void deleteLesson(String id);

    void editLesson(Lesson lesson);
}
