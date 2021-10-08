package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.service.AudienceService;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.AudienceDao;
import ua.com.foxminded.university.spring.dao.Page;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AudienceServiceImpl implements AudienceService {
    private final AudienceDao audienceDao;
    private final Validator<Audience> validator;

    @Override
    public void addAudience(Audience audience) {
        log.info("Adding audience started");
        log.info("Checking audience number existence in database");
        if (audienceDao.findByNumber(audience.getNumber()).isPresent()) {
            log.error("Audience already exists in database");
            throw new EntityAlreadyExistException("Entity with specified number is already exists");
        }
        log.info("Validating audience...");
        validator.validate(audience);
        audienceDao.save(audience);
        log.info("Audience successfully added");
    }

    @Override
    public Audience findAudienceById(String id) {
        log.info("Finding audience by id in database started");
        return audienceDao.findById(id).orElseThrow(() -> {
            log.error("Audience by id not found");
            return new EntityNotFoundException("No specified entity found");
        });
    }

    @Override
    public Audience findAudienceByNumber(Integer number) {
        log.info("Finding audience by number in database started");
        return audienceDao.findByNumber(number).orElseThrow(() -> {
            log.error("Audience by number not found");
            return new EntityNotFoundException("No specified entity found");
        });
    }

    @Override
    public List<Audience> showAllAudiences(Page page) {
        log.info("Getting all audience started");
        return audienceDao.findAll(page);

    }

    @Override
    public void deleteAudience(String id) {
        log.info("Audience deletion by id started");
        if (!audienceDao.findById(id).isPresent()) {
            log.error("No audience found for deletion");
            throw new EntityNotFoundException("No specified entity found");
        }
        audienceDao.deleteById(id);
        log.info("Successful audience deletion");
    }

    @Override
    public void editAudience(Audience audience) {
        log.info("Audience editing started");
        log.info("Checking audience existence");
        Optional<Audience> audienceDb = audienceDao.findById(audience.getId());
        if (!audienceDb.isPresent()) {
            log.error("Audience to edit not found");
            throw new EntityNotFoundException("No specified entity found");
        }
        Audience audienceToEdit = audienceDb.get();
        if (isNumberChanged(audience, audienceToEdit)) {
            log.info("Number has been changed, checking if same number already exists");
            if (audienceDao.findByNumber(audience.getNumber()).isPresent()) {
                log.error("Number already exists");
                throw new EntityAlreadyExistException("Specified number already exists");
            }
        }
        log.info("Validating audience");
        validator.validate(audience);
        audienceDao.update(audience);
        log.info("Audience successfully edited");
    }

    protected boolean isNumberChanged(Audience audience, Audience audienceToEdit) {
        return !Objects.equals(audienceToEdit.getNumber(), audience.getNumber());
    }
}
