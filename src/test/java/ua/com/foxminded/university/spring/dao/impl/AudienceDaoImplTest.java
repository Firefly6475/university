package ua.com.foxminded.university.spring.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.spring.config.JdbcConfigTest;
import ua.com.foxminded.university.spring.dao.AudienceDao;
import ua.com.foxminded.university.spring.dao.Page;
import ua.com.foxminded.university.spring.dao.mapper.AudienceMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AudienceDaoImplTest {
    private final JdbcConfigTest jdbcConfigTest = new JdbcConfigTest();
    private final AudienceMapper audienceMapper = new AudienceMapper();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(jdbcConfigTest.getTestDataSource());
    private final AudienceDao audienceDao = new AudienceDaoImpl(jdbcTemplate, audienceMapper);

    @Test
    void saveShouldInsertAudienceInDB() {
        Audience expectedAudience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withFloor(5)
                .withNumber(505)
                .build();
        audienceDao.save(expectedAudience);
        Audience actualAudience = audienceDao.findById(expectedAudience.getId()).get();

        assertEquals(expectedAudience, actualAudience);
    }

    @Test
    void saveAllShouldInsertListOfAudiencesInDB() {
        Audience audience1 = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withFloor(6)
                .withNumber(605)
                .build();
        Audience audience2 = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withFloor(5)
                .withNumber(505)
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
                .withId("aabb")
                .withFloor(2)
                .withNumber(261)
                .build();
        assertNotEquals(audience.getNumber(), expectedAudience.getNumber());

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

    @Test
    void findByNumberShouldReturnAudienceWithSpecifiedNumber() {
        Audience expectedAudience = Audience.builder()
                .withId(UUID.randomUUID().toString())
                .withFloor(1)
                .withNumber(150)
                .build();
        audienceDao.save(expectedAudience);
        Audience actualAudience = audienceDao.findByNumber(150).get();
        assertEquals(expectedAudience, actualAudience);
    }

    @Test
    void findByNumberShouldReturnOptionalEmptyIfThereIsNoAudienceWithGivenNumber() {
        Optional<Audience> expectedAudience = Optional.empty();
        Optional<Audience> actualAudience = audienceDao.findByNumber(123456);

        assertEquals(expectedAudience, actualAudience);
    }
}
