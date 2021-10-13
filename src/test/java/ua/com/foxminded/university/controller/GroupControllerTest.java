package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

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
public class GroupControllerTest {
    @Mock
    GroupService groupService;
    @InjectMocks
    GroupController groupController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void showAllGroupsShouldShowGroupsIfThereIsLessThan10Groups() throws Exception {
        int pageNumber = 1;
        List<Group> groups = new ArrayList<>();
        groups.add(Group.builder()
                .withId(UUID.randomUUID().toString())
                .build());
        groups.add(Group.builder()
                .withId(UUID.randomUUID().toString())
                .build());

        when(groupService.showAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/groups/list?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("group/groupsList"))
                .andExpect(model().attribute("groups", hasSize(2)));

        verify(groupService).showAllGroups();
    }

    @Test
    void showAllGroupsShouldShowGroupsIfThereIsMoreThan10GroupsFirstPage() throws Exception {
        int pageNumber = 1;
        List<Group> groups = new ArrayList<>();
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .build();
        for (int i = 0; i < 12; i++) {
            groups.add(group);
        }
        when(groupService.showAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/groups/list?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("group/groupsList"))
                .andExpect(model().attribute("groups", hasSize(10)));

        verify(groupService).showAllGroups();
    }

    @Test
    void showAllGroupsShouldShowGroupsIfThereIsMoreThan10GroupsSecondPage() throws Exception {
        int pageNumber = 2;
        List<Group> groups = new ArrayList<>();
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .build();
        for (int i = 0; i < 12; i++) {
            groups.add(group);
        }
        when(groupService.showAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/groups/list?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("group/groupsList"))
                .andExpect(model().attribute("groups", hasSize(2)));

        verify(groupService).showAllGroups();
    }

    @Test
    void showAllGroupsShouldThrowWrongPageNumberExceptionIfPageNumberIsZeroOrLess() throws Exception {
        int pageNumber = 0;
        List<Group> groups = new ArrayList<>();
        groups.add(Group.builder()
                .withId(UUID.randomUUID().toString())
                .build());

        when(groupService.showAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/groups/list?page=" + pageNumber))
                .andExpect(status().isNotAcceptable());

        verify(groupService).showAllGroups();
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void showAllGroupsShouldThrowWrongPageNumberExceptionIfPageNumberIsMoreThanExists() throws Exception {
        int pageNumber = 2;
        List<Group> groups = new ArrayList<>();
        groups.add(Group.builder()
                .withId(UUID.randomUUID().toString())
                .build());

        when(groupService.showAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/groups/list?page=" + pageNumber))
                .andExpect(status().isNotAcceptable());

        verify(groupService).showAllGroups();
        verifyNoMoreInteractions(groupService);
    }
}
