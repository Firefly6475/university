package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.service.DepartmentService;

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
public class DepartmentControllerTest {
    @Mock
    DepartmentService departmentService;
    @InjectMocks
    DepartmentController departmentController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
    }

    @Test
    void showAllDepartmentsShouldShowDepartmentsIfThereIsLessThan10Departments() throws Exception {
        int pageNumber = 1;
        List<Department> departments = new ArrayList<>();
        departments.add(Department.builder()
                .withId(UUID.randomUUID().toString())
                .build());
        departments.add(Department.builder()
                .withId(UUID.randomUUID().toString())
                .build());

        when(departmentService.showAllDepartments()).thenReturn(departments);

        mockMvc.perform(get("/department/all?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("department/departmentsList"))
                .andExpect(model().attribute("departments", hasSize(2)));

        verify(departmentService).showAllDepartments();
    }

    @Test
    void showAllDepartmentsShouldShowDepartmentsIfThereIsMoreThan10DepartmentsFirstPage() throws Exception {
        int pageNumber = 1;
        List<Department> departments = new ArrayList<>();
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .build();
        for (int i = 0; i < 12; i++) {
            departments.add(department);
        }
        when(departmentService.showAllDepartments()).thenReturn(departments);

        mockMvc.perform(get("/department/all?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("department/departmentsList"))
                .andExpect(model().attribute("departments", hasSize(10)));

        verify(departmentService).showAllDepartments();
    }

    @Test
    void showAllDepartmentsShouldShowDepartmentsIfThereIsMoreThan10DepartmentsSecondPage() throws Exception {
        int pageNumber = 2;
        List<Department> departments = new ArrayList<>();
        Department department = Department.builder()
                .withId(UUID.randomUUID().toString())
                .build();
        for (int i = 0; i < 12; i++) {
            departments.add(department);
        }
        when(departmentService.showAllDepartments()).thenReturn(departments);

        mockMvc.perform(get("/department/all?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("department/departmentsList"))
                .andExpect(model().attribute("departments", hasSize(2)));

        verify(departmentService).showAllDepartments();
    }
}
