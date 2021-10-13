package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.LessonType;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.exception.InvalidLessonDateException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.AudienceDao;
import ua.com.foxminded.university.spring.dao.DisciplineDao;
import ua.com.foxminded.university.spring.dao.GroupDao;
import ua.com.foxminded.university.spring.dao.LessonDao;
import ua.com.foxminded.university.spring.dao.Page;
import ua.com.foxminded.university.spring.dao.TeacherDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {
    private final Discipline discipline = Discipline.builder()
            .withId(UUID.randomUUID().toString())
            .withName("Java")
            .withTeachers(new ArrayList<>())
            .build();
    private final Audience audience = Audience.builder()
            .withId(UUID.randomUUID().toString())
            .withFloor(1)
            .withNumber(124)
            .build();
    private final Group group = Group.builder()
            .withId(UUID.randomUUID().toString())
            .withName("PR-14")
            .withCourse(3)
            .withStudents(new ArrayList<>())
            .build();
    private final Teacher teacher = Teacher.builder()
            .withId(UUID.randomUUID().toString())
            .withEmail("teacher@gmail.com")
            .withPassword("hello")
            .withBirthday(LocalDate.parse("1996-01-12"))
            .withName("Alex")
            .build();
    @Mock
    private LessonDao lessonDao;
    @Mock
    private DisciplineDao disciplineDao;
    @Mock
    private AudienceDao audienceDao;
    @Mock
    private GroupDao groupDao;
    @Mock
    private TeacherDao teacherDao;
    @Mock
    private Validator<Lesson> validator;
    @InjectMocks
    private LessonServiceImpl lessonService;

    @Test
    void addLessonShouldSaveLesson() {
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(disciplineDao.findByName(lesson.getDiscipline().getName())).thenReturn(Optional.of(lesson.getDiscipline()));
        when(audienceDao.findByNumber(lesson.getAudience().getNumber())).thenReturn(Optional.of(lesson.getAudience()));
        when(groupDao.findByName(lesson.getGroup().getName())).thenReturn(Optional.of(lesson.getGroup()));
        when(teacherDao.findByEmail(lesson.getTeacher().getEmail())).thenReturn(Optional.of(lesson.getTeacher()));
        doNothing().when(validator).validate(lesson);
        doNothing().when(lessonDao).save(lesson);

        lessonService.addLesson(lesson);

        verify(disciplineDao).findByName(lesson.getDiscipline().getName());
        verify(audienceDao).findByNumber(lesson.getAudience().getNumber());
        verify(groupDao).findByName(lesson.getGroup().getName());
        verify(teacherDao).findByEmail(lesson.getTeacher().getEmail());
        verify(validator).validate(lesson);
        verify(lessonDao).save(lesson);
    }

    @Test
    void addLessonShouldThrowEntityNotFoundExceptionIfDisciplineIsNotExists() {
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(disciplineDao.findByName(lesson.getDiscipline().getName())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonService.addLesson(lesson));

        String expectedMessage = "Specified discipline not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findByName(lesson.getDiscipline().getName());
        verifyNoInteractions(audienceDao);
        verifyNoInteractions(groupDao);
        verifyNoInteractions(teacherDao);
        verifyNoInteractions(validator);
        verifyNoInteractions(lessonDao);
    }

    @Test
    void addLessonShouldThrowEntityNotFoundExceptionIfAudienceIsNotExists() {
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(disciplineDao.findByName(lesson.getDiscipline().getName())).thenReturn(Optional.of(lesson.getDiscipline()));
        when(audienceDao.findByNumber(lesson.getAudience().getNumber())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonService.addLesson(lesson));

        String expectedMessage = "Specified audience not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findByName(lesson.getDiscipline().getName());
        verify(audienceDao).findByNumber(lesson.getAudience().getNumber());
        verifyNoInteractions(groupDao);
        verifyNoInteractions(teacherDao);
        verifyNoInteractions(validator);
        verifyNoInteractions(lessonDao);
    }

    @Test
    void addLessonShouldThrowEntityNotFoundExceptionIfGroupIsNotExists() {
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(disciplineDao.findByName(lesson.getDiscipline().getName())).thenReturn(Optional.of(lesson.getDiscipline()));
        when(audienceDao.findByNumber(lesson.getAudience().getNumber())).thenReturn(Optional.of(lesson.getAudience()));
        when(groupDao.findByName(lesson.getGroup().getName())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonService.addLesson(lesson));

        String expectedMessage = "Specified group not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findByName(lesson.getDiscipline().getName());
        verify(audienceDao).findByNumber(lesson.getAudience().getNumber());
        verify(groupDao).findByName(lesson.getGroup().getName());
        verifyNoInteractions(teacherDao);
        verifyNoInteractions(validator);
        verifyNoInteractions(lessonDao);
    }

    @Test
    void addLessonShouldThrowEntityNotFoundExceptionIfTeacherIsNotExists() {
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(disciplineDao.findByName(lesson.getDiscipline().getName())).thenReturn(Optional.of(lesson.getDiscipline()));
        when(audienceDao.findByNumber(lesson.getAudience().getNumber())).thenReturn(Optional.of(lesson.getAudience()));
        when(groupDao.findByName(lesson.getGroup().getName())).thenReturn(Optional.of(lesson.getGroup()));
        when(teacherDao.findByEmail(lesson.getTeacher().getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonService.addLesson(lesson));

        String expectedMessage = "Specified teacher not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findByName(lesson.getDiscipline().getName());
        verify(audienceDao).findByNumber(lesson.getAudience().getNumber());
        verify(groupDao).findByName(lesson.getGroup().getName());
        verify(teacherDao).findByEmail(lesson.getTeacher().getEmail());
        verifyNoInteractions(validator);
        verifyNoInteractions(lessonDao);
    }

    @Test
    void addLessonShouldThrowInvalidLessonDateExceptionIfSpecifiedDateIsBeforeCurrentTime() {
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2011-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(disciplineDao.findByName(lesson.getDiscipline().getName())).thenReturn(Optional.of(lesson.getDiscipline()));
        when(audienceDao.findByNumber(lesson.getAudience().getNumber())).thenReturn(Optional.of(lesson.getAudience()));
        when(groupDao.findByName(lesson.getGroup().getName())).thenReturn(Optional.of(lesson.getGroup()));
        when(teacherDao.findByEmail(lesson.getTeacher().getEmail())).thenReturn(Optional.of(lesson.getTeacher()));
        doThrow(new InvalidLessonDateException("Lesson date is before current date")).when(validator).validate(lesson);

        Exception exception = assertThrows(InvalidLessonDateException.class, () -> lessonService.addLesson(lesson));

        String expectedMessage = "Lesson date is before current date";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(disciplineDao).findByName(lesson.getDiscipline().getName());
        verify(audienceDao).findByNumber(lesson.getAudience().getNumber());
        verify(groupDao).findByName(lesson.getGroup().getName());
        verify(teacherDao).findByEmail(lesson.getTeacher().getEmail());
        verify(validator).validate(lesson);
        verifyNoInteractions(lessonDao);
    }

    @Test
    void findLessonByIdShouldReturnEntityWithSpecifiedId() {
        Lesson expectedLesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(lessonDao.findById(expectedLesson.getId())).thenReturn(Optional.of(expectedLesson));

        Lesson actualLesson = lessonService.findLessonById(expectedLesson.getId());
        assertEquals(expectedLesson, actualLesson);

        verify(lessonDao).findById(expectedLesson.getId());
    }

    @Test
    void findLessonByIdShouldThrowEntityNotFoundExceptionIfLessonIsNotExists() {
        String lessonId = "hello";

        when(lessonDao.findById(lessonId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonService.findLessonById(lessonId));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(lessonDao).findById(lessonId);
    }

    @Test
    void findLessonByDisciplineNameShouldReturnEntityWithSpecifiedId() {
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();
        List<Lesson> expectedLessons = new ArrayList<>();
        expectedLessons.add(lesson);

        when(lessonDao.findByDisciplineName(lesson.getDiscipline().getName())).thenReturn(expectedLessons);

        List<Lesson> actualLessons = lessonService.findLessonsByDisciplineName(lesson.getDiscipline().getName());
        assertEquals(expectedLessons, actualLessons);

        verify(lessonDao).findByDisciplineName(lesson.getDiscipline().getName());
    }

    @Test
    void findLessonByDisciplineNameShouldThrowEntityNotFoundExceptionIfLessonIsNotExists() {
        String disciplineName = "hello";

        when(lessonDao.findByDisciplineName(disciplineName)).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonService.findLessonsByDisciplineName(disciplineName));

        String expectedMessage = "No specified entities found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(lessonDao).findByDisciplineName(disciplineName);
    }

    @Test
    void showAllLessonsShouldReturnAllLessons() {
        List<Lesson> lessons = new ArrayList<>();

        when(lessonDao.findAll()).thenReturn(lessons);

        lessons = lessonService.showAllLessons();

        verify(lessonDao).findAll();
    }

    @Test
    void deleteLessonShouldRemoveLessonFromDB() {
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(lessonDao.findById(lesson.getId())).thenReturn(Optional.of(lesson));
        doNothing().when(lessonDao).deleteById(lesson.getId());

        lessonService.deleteLesson(lesson.getId());

        verify(lessonDao).findById(lesson.getId());
        verify(lessonDao).deleteById(lesson.getId());
    }

    @Test
    void deleteLessonShouldThrowEntityNotFoundExceptionIfSpecifiedEntityNotExists() {
        String lessonId = "hello";

        when(lessonDao.findById(lessonId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonService.deleteLesson(lessonId));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(lessonDao).findById(lessonId);
        verifyNoMoreInteractions(lessonDao);
    }

    @Test
    void editLessonShouldUpdateLesson() {
        Lesson updatedLesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-12-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();
        Lesson lessonToUpdate = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(lessonDao.findById(updatedLesson.getId())).thenReturn(Optional.of(lessonToUpdate));
        when(disciplineDao.findByName(updatedLesson.getDiscipline().getName())).thenReturn(Optional.of(updatedLesson.getDiscipline()));
        when(audienceDao.findByNumber(updatedLesson.getAudience().getNumber())).thenReturn(Optional.of(updatedLesson.getAudience()));
        when(groupDao.findByName(updatedLesson.getGroup().getName())).thenReturn(Optional.of(updatedLesson.getGroup()));
        when(teacherDao.findByEmail(updatedLesson.getTeacher().getEmail())).thenReturn(Optional.of(updatedLesson.getTeacher()));
        doNothing().when(validator).validate(updatedLesson);
        doNothing().when(lessonDao).update(updatedLesson);

        lessonService.editLesson(updatedLesson);

        verify(lessonDao).findById(updatedLesson.getId());
        verify(disciplineDao).findByName(updatedLesson.getDiscipline().getName());
        verify(audienceDao).findByNumber(updatedLesson.getAudience().getNumber());
        verify(groupDao).findByName(updatedLesson.getGroup().getName());
        verify(teacherDao).findByEmail(updatedLesson.getTeacher().getEmail());
        verify(validator).validate(updatedLesson);
        verify(lessonDao).update(updatedLesson);
    }

    @Test
    void editLessonShouldThrowEntityNotFoundExceptionIfSpecifiedLessonIsNotExists() {
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(lessonDao.findById(lesson.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonService.editLesson(lesson));

        String expectedMessage = "No specified entity found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(lessonDao).findById(lesson.getId());
        verifyNoInteractions(disciplineDao);
        verifyNoInteractions(audienceDao);
        verifyNoInteractions(groupDao);
        verifyNoInteractions(teacherDao);
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(lessonDao);
    }

    @Test
    void editLessonShouldThrowEntityNotFoundExceptionIfSpecifiedDisciplineIsNotExists() {
        Lesson updatedLesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-12-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();
        Lesson lessonToUpdate = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(lessonDao.findById(updatedLesson.getId())).thenReturn(Optional.of(lessonToUpdate));
        when(disciplineDao.findByName(updatedLesson.getDiscipline().getName())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonService.editLesson(updatedLesson));

        String expectedMessage = "Specified discipline not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(lessonDao).findById(updatedLesson.getId());
        verify(disciplineDao).findByName(updatedLesson.getDiscipline().getName());
        verifyNoInteractions(audienceDao);
        verifyNoInteractions(groupDao);
        verifyNoInteractions(teacherDao);
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(lessonDao);
    }

    @Test
    void editLessonShouldThrowEntityNotFoundExceptionIfSpecifiedAudienceIsNotExists() {
        Lesson updatedLesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-12-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();
        Lesson lessonToUpdate = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(lessonDao.findById(updatedLesson.getId())).thenReturn(Optional.of(lessonToUpdate));
        when(disciplineDao.findByName(updatedLesson.getDiscipline().getName())).thenReturn(Optional.of(updatedLesson.getDiscipline()));
        when(audienceDao.findByNumber(updatedLesson.getAudience().getNumber())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonService.editLesson(updatedLesson));

        String expectedMessage = "Specified audience not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(lessonDao).findById(updatedLesson.getId());
        verify(disciplineDao).findByName(updatedLesson.getDiscipline().getName());
        verify(audienceDao).findByNumber(updatedLesson.getAudience().getNumber());
        verifyNoInteractions(groupDao);
        verifyNoInteractions(teacherDao);
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(lessonDao);
    }

    @Test
    void editLessonShouldThrowEntityNotFoundExceptionIfSpecifiedGroupIsNotExists() {
        Lesson updatedLesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-12-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();
        Lesson lessonToUpdate = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(lessonDao.findById(updatedLesson.getId())).thenReturn(Optional.of(lessonToUpdate));
        when(disciplineDao.findByName(updatedLesson.getDiscipline().getName())).thenReturn(Optional.of(updatedLesson.getDiscipline()));
        when(audienceDao.findByNumber(updatedLesson.getAudience().getNumber())).thenReturn(Optional.of(updatedLesson.getAudience()));
        when(groupDao.findByName(updatedLesson.getGroup().getName())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonService.editLesson(updatedLesson));

        String expectedMessage = "Specified group not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(lessonDao).findById(updatedLesson.getId());
        verify(disciplineDao).findByName(updatedLesson.getDiscipline().getName());
        verify(audienceDao).findByNumber(updatedLesson.getAudience().getNumber());
        verify(groupDao).findByName(updatedLesson.getGroup().getName());
        verifyNoInteractions(teacherDao);
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(lessonDao);
    }

    @Test
    void editLessonShouldThrowEntityNotFoundExceptionIfSpecifiedTeacherIsNotExists() {
        Lesson updatedLesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-12-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();
        Lesson lessonToUpdate = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .withDiscipline(discipline)
                .withAudience(audience)
                .withGroup(group)
                .withTeacher(teacher)
                .withLessonType(LessonType.LECTURE)
                .withDate(LocalDate.parse("2021-11-11"))
                .withTimeStart(LocalTime.parse("12:00:00"))
                .withTimeEnd(LocalTime.parse("13:30:00"))
                .build();

        when(lessonDao.findById(updatedLesson.getId())).thenReturn(Optional.of(lessonToUpdate));
        when(disciplineDao.findByName(updatedLesson.getDiscipline().getName())).thenReturn(Optional.of(updatedLesson.getDiscipline()));
        when(audienceDao.findByNumber(updatedLesson.getAudience().getNumber())).thenReturn(Optional.of(updatedLesson.getAudience()));
        when(groupDao.findByName(updatedLesson.getGroup().getName())).thenReturn(Optional.of(updatedLesson.getGroup()));
        when(teacherDao.findByEmail(updatedLesson.getTeacher().getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonService.editLesson(updatedLesson));

        String expectedMessage = "Specified teacher not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(lessonDao).findById(updatedLesson.getId());
        verify(disciplineDao).findByName(updatedLesson.getDiscipline().getName());
        verify(audienceDao).findByNumber(updatedLesson.getAudience().getNumber());
        verify(groupDao).findByName(updatedLesson.getGroup().getName());
        verify(teacherDao).findByEmail(updatedLesson.getTeacher().getEmail());
        verifyNoInteractions(validator);
        verifyNoMoreInteractions(lessonDao);
    }
}
