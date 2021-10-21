package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.service.LessonService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class LessonControllerTest {
    @Mock
    LessonService lessonService;
    @InjectMocks
    LessonController lessonController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController).build();
    }

    @Test
    void showAllLessonsShouldShowLessonsIfThereIsLessThan10Lessons() throws Exception {
        int pageNumber = 1;
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .build());
        lessons.add(Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .build());

        when(lessonService.showAllLessons()).thenReturn(lessons);

        mockMvc.perform(get("/lesson/all?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("lesson/lessonsList"))
                .andExpect(model().attribute("lessons", hasSize(2)));

        verify(lessonService).showAllLessons();
    }

    @Test
    void showAllLessonsShouldShowLessonsIfThereIsMoreThan10LessonsFirstPage() throws Exception {
        int pageNumber = 1;
        List<Lesson> lessons = new ArrayList<>();
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .build();
        for (int i = 0; i < 12; i++) {
            lessons.add(lesson);
        }
        when(lessonService.showAllLessons()).thenReturn(lessons);

        mockMvc.perform(get("/lesson/all?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("lesson/lessonsList"))
                .andExpect(model().attribute("lessons", hasSize(10)));

        verify(lessonService).showAllLessons();
    }

    @Test
    void showAllLessonsShouldShowLessonsIfThereIsMoreThan10LessonsSecondPage() throws Exception {
        int pageNumber = 2;
        List<Lesson> lessons = new ArrayList<>();
        Lesson lesson = Lesson.builder()
                .withId(UUID.randomUUID().toString())
                .build();
        for (int i = 0; i < 12; i++) {
            lessons.add(lesson);
        }
        when(lessonService.showAllLessons()).thenReturn(lessons);

        mockMvc.perform(get("/lesson/all?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("lesson/lessonsList"))
                .andExpect(model().attribute("lessons", hasSize(2)));

        verify(lessonService).showAllLessons();
    }
}
