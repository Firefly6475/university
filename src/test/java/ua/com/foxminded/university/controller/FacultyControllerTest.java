package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.service.FacultyService;

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
public class FacultyControllerTest {
    @Mock
    FacultyService facultyService;
    @InjectMocks
    FacultyController facultyController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(facultyController).build();
    }

    @Test
    void showAllFacultiesShouldShowFacultiesIfThereIsLessThan10Faculties() throws Exception {
        int pageNumber = 1;
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .build());
        faculties.add(Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .build());

        when(facultyService.showAllFaculties()).thenReturn(faculties);

        mockMvc.perform(get("/faculties/list?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("faculty/facultiesList"))
                .andExpect(model().attribute("faculties", hasSize(2)));

        verify(facultyService).showAllFaculties();
    }

    @Test
    void showAllFacultiesShouldShowFacultiesIfThereIsMoreThan10FacultiesFirstPage() throws Exception {
        int pageNumber = 1;
        List<Faculty> faculties = new ArrayList<>();
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .build();
        for (int i = 0; i < 12; i++) {
            faculties.add(faculty);
        }
        when(facultyService.showAllFaculties()).thenReturn(faculties);

        mockMvc.perform(get("/faculties/list?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("faculty/facultiesList"))
                .andExpect(model().attribute("faculties", hasSize(10)));

        verify(facultyService).showAllFaculties();
    }

    @Test
    void showAllFacultiesShouldShowFacultiesIfThereIsMoreThan10FacultiesSecondPage() throws Exception {
        int pageNumber = 2;
        List<Faculty> faculties = new ArrayList<>();
        Faculty faculty = Faculty.builder()
                .withId(UUID.randomUUID().toString())
                .build();
        for (int i = 0; i < 12; i++) {
            faculties.add(faculty);
        }
        when(facultyService.showAllFaculties()).thenReturn(faculties);

        mockMvc.perform(get("/faculties/list?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("faculty/facultiesList"))
                .andExpect(model().attribute("faculties", hasSize(2)));

        verify(facultyService).showAllFaculties();
    }
}
