package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Lesson;

import java.util.List;

public interface LessonService {
    void addLesson(Lesson lesson);

    Lesson findLessonById(String id);

    List<Lesson> findLessonsByDisciplineName(String disciplineName);

    List<Lesson> showAllLessons();

    void deleteLesson(String id);

    void editLesson(Lesson lesson);
}
