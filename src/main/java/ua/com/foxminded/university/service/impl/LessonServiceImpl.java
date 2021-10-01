package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class LessonServiceImpl implements LessonService {
    private final LessonDao lessonDao;
    private final DisciplineDao disciplineDao;
    private final AudienceDao audienceDao;
    private final GroupDao groupDao;
    private final TeacherDao teacherDao;
    private final Validator<Lesson> validator;

    @Override
    public void addLesson(Lesson lesson) {
        log.info("Adding lesson started");
        log.info("Checking discipline existence");
        if (!disciplineDao.findByName(lesson.getDiscipline().getName()).isPresent()) {
            log.error("Discipline is not exists");
            throw new EntityNotFoundException("Specified discipline not found");
        }
        log.info("Checking audience existence");
        if (!audienceDao.findByNumber(lesson.getAudience().getNumber()).isPresent()) {
            log.error("Audience is not exists");
            throw new EntityNotFoundException("Specified audience not found");
        }
        log.info("Checking group existence");
        if (!groupDao.findByName(lesson.getGroup().getName()).isPresent()) {
            log.error("Group is not exists");
            throw new EntityNotFoundException("Specified group not found");
        }
        log.info("Checking teacher existence");
        if (!teacherDao.findByEmail(lesson.getTeacher().getEmail()).isPresent()) {
            log.error("Teacher is not exists");
            throw new EntityNotFoundException("Specified teacher not found");
        }
        log.info("Validating lesson...");
        validator.validate(lesson);
        lessonDao.save(lesson);
        log.info("Lesson successfully added");
    }

    @Override
    public Lesson findLessonById(String id) {
        log.info("Finding lesson by id in database started");
        return lessonDao.findById(id).orElseThrow(() -> {
            log.error("Lesson by id not found");
            return new EntityNotFoundException("No specified entity found");
        });
    }

    @Override
    public List<Lesson> findLessonsByDisciplineName(String disciplineName) {
        log.info("Finding lessons by discipline name started");
        List<Lesson> lessons = lessonDao.findByDisciplineName(disciplineName);
        if (lessons.isEmpty()) {
            log.error("No lessons by discipline name found");
            throw new EntityNotFoundException("No specified entities found");
        }
        return lessons;
    }

    @Override
    public List<Lesson> showAllLessons(Page page) {
        log.info("Getting all lessons started");
        return lessonDao.findAll(page);
    }

    @Override
    public void deleteLesson(String id) {
        log.info("Lesson deletion by id started");
        log.info("Checking lesson existence");
        if (!lessonDao.findById(id).isPresent()) {
            log.error("No lesson found for deletion");
            throw new EntityNotFoundException("No specified entity found");
        }
        lessonDao.deleteById(id);
        log.info("Successful lesson deletion");
    }

    @Override
    public void editLesson(Lesson lesson) {
        log.info("Lesson editing started");
        log.info("Checking lesson existence");
        if (!lessonDao.findById(lesson.getId()).isPresent()) {
            log.error("Lesson to edit not found");
            throw new EntityNotFoundException("No specified entity found");
        }
        log.info("Checking discipline existence");
        if (!disciplineDao.findByName(lesson.getDiscipline().getName()).isPresent()) {
            log.error("Discipline to edit in lesson not exists");
            throw new EntityNotFoundException("Specified discipline not found");
        }
        log.info("Checking audience existence");
        if (!audienceDao.findByNumber(lesson.getAudience().getNumber()).isPresent()) {
            log.error("Audience to edit in lesson not exists");
            throw new EntityNotFoundException("Specified audience not found");
        }
        log.info("Checking group existence");
        if (!groupDao.findByName(lesson.getGroup().getName()).isPresent()) {
            log.error("Group to edit in lesson not exists");
            throw new EntityNotFoundException("Specified group not found");
        }
        log.info("Checking teacher existence");
        if (!teacherDao.findByEmail(lesson.getTeacher().getEmail()).isPresent()) {
            log.error("Teacher to edit in lesson not exists");
            throw new EntityNotFoundException("Specified teacher not found");
        }
        log.info("Validating lesson...");
        validator.validate(lesson);
        lessonDao.update(lesson);
        log.info("Lesson successfully edited");
    }
}
