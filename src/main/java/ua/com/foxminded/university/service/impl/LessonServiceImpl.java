package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.AudienceDao;
import ua.com.foxminded.university.spring.dao.DisciplineDao;
import ua.com.foxminded.university.spring.dao.GroupDao;
import ua.com.foxminded.university.spring.dao.LessonDao;
import ua.com.foxminded.university.spring.dao.Page;
import ua.com.foxminded.university.spring.dao.TeacherDao;

import java.util.List;

@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonDao lessonDao;
    private final DisciplineDao disciplineDao;
    private final AudienceDao audienceDao;
    private final GroupDao groupDao;
    private final TeacherDao teacherDao;
    private final Validator<Lesson> validator;

    @Override
    public void addLesson(Lesson lesson) {
        if (!disciplineDao.findByName(lesson.getDiscipline().getName()).isPresent()) {
            throw new EntityNotFoundException("Specified discipline not found");
        }
        if (!audienceDao.findByNumber(lesson.getAudience().getNumber()).isPresent()) {
            throw new EntityNotFoundException("Specified audience not found");
        }
        if (!groupDao.findByName(lesson.getGroup().getName()).isPresent()) {
            throw new EntityNotFoundException("Specified group not found");
        }
        if (!teacherDao.findByEmail(lesson.getTeacher().getEmail()).isPresent()) {
            throw new EntityNotFoundException("Specified teacher not found");
        }

        validator.validate(lesson);
        lessonDao.save(lesson);
    }

    @Override
    public Lesson findLessonById(String id) {
        return lessonDao.findById(id).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));
    }

    @Override
    public List<Lesson> findLessonsByDisciplineName(String disciplineName) {
        List<Lesson> lessons = lessonDao.findByDisciplineName(disciplineName);
        if (lessons.isEmpty()) {
            throw new EntityNotFoundException("No specified entities found");
        }
        return lessons;
    }

    @Override
    public List<Lesson> showAllLessons(Page page) {
        return lessonDao.findAll(page);
    }

    @Override
    public void deleteLesson(String id) {
        if (!lessonDao.findById(id).isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        lessonDao.deleteById(id);
    }

    @Override
    public void editLesson(Lesson lesson) {
        if (!lessonDao.findById(lesson.getId()).isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        if (!disciplineDao.findByName(lesson.getDiscipline().getName()).isPresent()) {
            throw new EntityNotFoundException("Specified discipline not found");
        }
        if (!audienceDao.findByNumber(lesson.getAudience().getNumber()).isPresent()) {
            throw new EntityNotFoundException("Specified audience not found");
        }
        if (!groupDao.findByName(lesson.getGroup().getName()).isPresent()) {
            throw new EntityNotFoundException("Specified group not found");
        }
        if (!teacherDao.findByEmail(lesson.getTeacher().getEmail()).isPresent()) {
            throw new EntityNotFoundException("Specified teacher not found");
        }

        validator.validate(lesson);
        lessonDao.update(lesson);
    }
}
