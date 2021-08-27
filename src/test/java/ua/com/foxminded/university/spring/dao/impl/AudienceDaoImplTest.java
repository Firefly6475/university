package ua.com.foxminded.university.spring.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.spring.dao.mapper.AudienceMapper;
import ua.com.foxminded.university.spring.config.JdbcConfigTest;
import ua.com.foxminded.university.spring.dao.AudienceDao;
import ua.com.foxminded.university.spring.dao.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AudienceDaoImplTest {
    private final JdbcConfigTest jdbcConfigTest = new JdbcConfigTest();
    private final AudienceMapper audienceMapper = new AudienceMapper();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(jdbcConfigTest.getTestDataSource());
    private final AudienceDao audienceDao = new AudienceDaoImpl(jdbcTemplate, audienceMapper);

    @Test
    void saveShouldInsertAudienceInDB() {
        Audience expectedAudience = Audience.builder()
                .id(UUID.randomUUID().toString())
                .floor(5)
                .number(505)
                .build();
        audienceDao.save(expectedAudience);
        Audience actualAudience = audienceDao.findById(expectedAudience.getId()).get();

        assertEquals(expectedAudience, actualAudience);
    }

    @Test
    void saveAllShouldInsertListOfAudiencesInDB() {
        Audience audience1 = Audience.builder()
                .id(UUID.randomUUID().toString())
                .floor(6)
                .number(605)
                .build();
        Audience audience2 = Audience.builder()
                .id(UUID.randomUUID().toString())
                .floor(5)
                .number(505)
                .build();
        List<Audience> expectedAudiences = new ArrayList<>();
        expectedAudiences.add(audience1);
        expectedAudiences.add(audience2);
        audienceDao.saveAll(expectedAudiences);
        List<Audience> actualAudiences = audienceDao.findAll();

        assertTrue(actualAudiences.containsAll(expectedAudiences));
    }

    @Test
    void updateShouldChangeAudienceField() {
        Audience audience = audienceDao.findById("aabb").get();
        Audience expectedAudience = Audience.builder()
                .id("aabb")
                .floor(2)
                .number(261)
                .build();
        assertFalse(audience.getNumber().equals(expectedAudience.getNumber()));

        audienceDao.update(expectedAudience);
        Audience actualAudience = audienceDao.findById("aabb").get();

        assertEquals(expectedAudience, actualAudience);
    }

    @Test
    void findAllPagedShouldFindFirstPageWith2Results() {
        List<Audience> audiences = audienceDao.findAll();
        List<Audience> expectedAudiences = new ArrayList<>();
        expectedAudiences.add(audiences.get(0));
        expectedAudiences.add(audiences.get(1));

        Page page = new Page(1, 2);
        List<Audience> actualAudiences = audienceDao.findAll(page);

        assertEquals(expectedAudiences, actualAudiences);
    }

    @Test
    void deleteByIdShouldDeleteAudienceWithSpecifiedId() {
        Optional<Audience> audience = audienceDao.findById("ccdd");
        assertTrue(audience.isPresent());
        audienceDao.deleteById("ccdd");

        assertFalse(audienceDao.findById("ccdd").isPresent());
    }
}
