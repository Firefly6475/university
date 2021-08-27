package ua.com.foxminded.university.spring.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.LessonType;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.spring.dao.mapper.AudienceMapper;
import ua.com.foxminded.university.spring.dao.mapper.DisciplineMapper;
import ua.com.foxminded.university.spring.dao.mapper.GroupMapper;
import ua.com.foxminded.university.spring.dao.mapper.LessonMapper;
import ua.com.foxminded.university.spring.dao.mapper.StudentMapper;
import ua.com.foxminded.university.spring.dao.mapper.TeacherMapper;
import ua.com.foxminded.university.spring.config.JdbcConfigTest;
import ua.com.foxminded.university.spring.dao.AudienceDao;
import ua.com.foxminded.university.spring.dao.DisciplineDao;
import ua.com.foxminded.university.spring.dao.GroupDao;
import ua.com.foxminded.university.spring.dao.LessonDao;
import ua.com.foxminded.university.spring.dao.TeacherDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LessonDaoImplTest {
    private final TeacherMapper teacherMapper = new TeacherMapper();
    private final StudentMapper studentMapper = new StudentMapper();
    private final DisciplineMapper disciplineMapper = new DisciplineMapper(teacherMapper);
    private final GroupMapper groupMapper = new GroupMapper(studentMapper);
    private final AudienceMapper audienceMapper = new AudienceMapper();
    private final LessonMapper lessonMapper = new LessonMapper(disciplineMapper, audienceMapper, groupMapper,
            teacherMapper, studentMapper);
    private final JdbcConfigTest jdbcConfigTest = new JdbcConfigTest();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(jdbcConfigTest.getTestDataSource());
    private final TeacherDao teacherDao = new TeacherDaoImpl(jdbcTemplate, teacherMapper);
    private final AudienceDao audienceDao = new AudienceDaoImpl(jdbcTemplate, audienceMapper);
    private final GroupDao groupDao = new GroupDaoImpl(jdbcTemplate, groupMapper);
    private final DisciplineDao disciplineDao = new DisciplineDaoImpl(jdbcTemplate, disciplineMapper);
    private final LessonDao lessonDao = new LessonDaoImpl(jdbcTemplate, lessonMapper);

    @Test
    void saveShouldInsertLessonInDB() {
        Audience audience = audienceDao.findById("aabb").get();
        Discipline discipline = disciplineDao.findById("aabb").get();
        Teacher teacher = teacherDao.findById("aabb").get();
        Group group = groupDao.findById("aabb").get();

        Lesson expectedLesson = Lesson.builder()
                .id("eeff")
                .audience(audience)
                .discipline(discipline)
                .lessonType(LessonType.LECTURE)
                .date(LocalDate.parse("2001-01-01"))
                .timeStart(LocalTime.parse("12:00:00"))
                .timeEnd(LocalTime.parse("13:00:00"))
                .teacher(teacher)
                .group(group)
                .build();
        lessonDao.save(expectedLesson);
        Lesson actualLesson = lessonDao.findById("eeff").get();

        assertEquals(expectedLesson, actualLesson);
    }

    @Test
    void saveAllShouldInsertListOfLessonsInDB() {
        Audience audience = audienceDao.findById("aabb").get();
        Discipline discipline = disciplineDao.findById("aabb").get();
        Teacher teacher = teacherDao.findById("aabb").get();
        Group group = groupDao.findById("aabb").get();

        Lesson lesson1 = Lesson.builder()
                .id("eeff")
                .audience(audience)
                .discipline(discipline)
                .lessonType(LessonType.LECTURE)
                .date(LocalDate.parse("2001-01-01"))
                .timeStart(LocalTime.parse("12:00:00"))
                .timeEnd(LocalTime.parse("13:00:00"))
                .teacher(teacher)
                .group(group)
                .build();
        Lesson lesson2 = Lesson.builder()
                .id("ffgg")
                .audience(audience)
                .discipline(discipline)
                .lessonType(LessonType.LAB)
                .date(LocalDate.parse("2001-01-01"))
                .timeStart(LocalTime.parse("14:00:00"))
                .timeEnd(LocalTime.parse("15:00:00"))
                .teacher(teacher)
                .group(group)
                .build();
        List<Lesson> expectedLessons = new ArrayList<>();
        expectedLessons.add(lesson1);
        expectedLessons.add(lesson2);

        lessonDao.saveAll(expectedLessons);
        List<Lesson> actualLessons = lessonDao.findAll();

        assertTrue(actualLessons.containsAll(expectedLessons));
    }

    @Test
    void updateLessonShouldChangeLessonFieldInDB() {
        Lesson lesson = lessonDao.findById("aabb").get();
        Lesson expectedLesson = Lesson.builder()
                .id(lesson.getId())
                .audience(lesson.getAudience())
                .discipline(lesson.getDiscipline())
                .lessonType(LessonType.PRACTICE)
                .date(LocalDate.parse("2002-01-01"))
                .timeStart(LocalTime.parse("08:00:00"))
                .timeEnd(LocalTime.parse("09:30:00"))
                .teacher(lesson.getTeacher())
                .group(lesson.getGroup())
                .build();
        assertNotEquals(expectedLesson, lesson);

        lessonDao.update(expectedLesson);
        Lesson actualLesson = lessonDao.findById("aabb").get();

        assertEquals(expectedLesson, actualLesson);
    }
}
