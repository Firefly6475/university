package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.service.DisciplineService;

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
public class DisciplineControllerTest {
    @Mock
    DisciplineService disciplineService;
    @InjectMocks
    DisciplineController disciplineController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(disciplineController).build();
    }

    @Test
    void showAllDisciplinesShouldShowDisciplinesIfThereIsLessThan10Disciplines() throws Exception {
        int pageNumber = 1;
        List<Discipline> disciplines = new ArrayList<>();
        disciplines.add(Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .build());
        disciplines.add(Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .build());

        when(disciplineService.showAllDisciplines()).thenReturn(disciplines);

        mockMvc.perform(get("/discipline/all?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("discipline/disciplinesList"))
                .andExpect(model().attribute("disciplines", hasSize(2)));

        verify(disciplineService).showAllDisciplines();
    }

    @Test
    void showAllDisciplinesShouldShowDisciplinesIfThereIsMoreThan10DisciplinesFirstPage() throws Exception {
        int pageNumber = 1;
        List<Discipline> disciplines = new ArrayList<>();
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .build();
        for (int i = 0; i < 12; i++) {
            disciplines.add(discipline);
        }
        when(disciplineService.showAllDisciplines()).thenReturn(disciplines);

        mockMvc.perform(get("/discipline/all?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("discipline/disciplinesList"))
                .andExpect(model().attribute("disciplines", hasSize(10)));

        verify(disciplineService).showAllDisciplines();
    }

    @Test
    void showAllDisciplinesShouldShowDisciplinesIfThereIsMoreThan10DisciplinesSecondPage() throws Exception {
        int pageNumber = 2;
        List<Discipline> disciplines = new ArrayList<>();
        Discipline discipline = Discipline.builder()
                .withId(UUID.randomUUID().toString())
                .build();
        for (int i = 0; i < 12; i++) {
            disciplines.add(discipline);
        }
        when(disciplineService.showAllDisciplines()).thenReturn(disciplines);

        mockMvc.perform(get("/discipline/all?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("discipline/disciplinesList"))
                .andExpect(model().attribute("disciplines", hasSize(2)));

        verify(disciplineService).showAllDisciplines();
    }
}
