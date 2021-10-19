package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {
    @Mock
    StudentService studentService;

    @InjectMocks
    StudentController studentController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void showAllStudentsShouldShowStudentsOnFirstPage() throws Exception {
        Integer pageNumber = 1;
        List<Student> students = new ArrayList<>();
        students.add(Student.builder()
                .withId(UUID.randomUUID().toString())
                .withEmail("hello@gmail.com")
                .withPassword("myPass")
                .build());

        when(studentService.showAllStudents()).thenReturn(students);
        when(studentService.showAllStudents(pageNumber)).thenReturn(students);

        mockMvc.perform(get("/students/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/studentsList"))
                .andExpect(model().attribute("students", hasSize(1)));

        verify(studentService).showAllStudents();
        verify(studentService).showAllStudents(pageNumber);
    }
}
