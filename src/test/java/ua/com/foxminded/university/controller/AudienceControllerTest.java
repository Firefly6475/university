package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.service.AudienceService;

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
public class AudienceControllerTest {
    @Mock
    AudienceService audienceService;

    @InjectMocks
    AudienceController audienceController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(audienceController).build();
    }

    @Test
    void showAllAudiencesShouldShowAudiencesOnFirstPage() throws Exception {
        Integer pageNumber = 1;
        List<Audience> audiences = new ArrayList<>();
        audiences.add(Audience.builder()
                .withId(UUID.randomUUID().toString())
                .build());

        when(audienceService.showAllAudiences()).thenReturn(audiences);
        when(audienceService.showAllAudiences(pageNumber)).thenReturn(audiences);

        mockMvc.perform(get("/audiences/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("audience/audiencesList"))
                .andExpect(model().attribute("audiences", hasSize(1)));

        verify(audienceService).showAllAudiences();
        verify(audienceService).showAllAudiences(pageNumber);
    }

    @Test
    void showAllAudiencesShouldThrowWrongPageNumberExceptionIfPageNumberIsZeroOrLess() throws Exception {
        int pageNumber = 0;
        List<Audience> audiences = new ArrayList<>();
        audiences.add(Audience.builder()
                .withId(UUID.randomUUID().toString())
                .build());

        when(audienceService.showAllAudiences()).thenReturn(audiences);

        mockMvc.perform(get("/audiences/list?page=" + pageNumber))
                .andExpect(status().isNotAcceptable());

        verify(audienceService).showAllAudiences();
        verifyNoMoreInteractions(audienceService);
    }

    @Test
    void showAllAudiencesShouldThrowWrongPageNumberExceptionIfPageNumberIsMoreThanExists() throws Exception {
        int pageNumber = 2;
        List<Audience> audiences = new ArrayList<>();
        audiences.add(Audience.builder()
                .withId(UUID.randomUUID().toString())
                .build());

        when(audienceService.showAllAudiences()).thenReturn(audiences);

        mockMvc.perform(get("/audiences/list?page=" + pageNumber))
                .andExpect(status().isNotAcceptable());

        verify(audienceService).showAllAudiences();
        verifyNoMoreInteractions(audienceService);
    }
}