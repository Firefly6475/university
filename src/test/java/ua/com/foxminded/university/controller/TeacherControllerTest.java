package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

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
public class TeacherControllerTest {
    @Mock
    TeacherService teacherService;

    @InjectMocks
    TeacherController teacherController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void showAllTeachersShouldShowTeachersOnFirstPage() throws Exception {
        int pageNumber = 1;
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(Teacher.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("hello@gmail.com")
                .withPassword("myPass")
                .build());

        when(teacherService.showAllTeachers()).thenReturn(teachers);
        when(teacherService.showAllTeachers(pageNumber)).thenReturn(teachers);

        mockMvc.perform(get("/teacher/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/teachersList"))
                .andExpect(model().attribute("teachers", hasSize(1)));

        verify(teacherService).showAllTeachers();
        verify(teacherService).showAllTeachers(pageNumber);
    }
}
