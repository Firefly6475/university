package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
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
public class AudienceServiceImpl implements AudienceService {
    private final AudienceDao audienceDao;
    private final Validator<Audience> validator;

    @Override
    public void addAudience(Audience audience) {
        if (audienceDao.findByNumber(audience.getNumber()).isPresent()) {
            throw new EntityAlreadyExistException("Entity with specified number is already exists");
        }
        validator.validate(audience);
        audienceDao.save(audience);
    }

    @Override
    public Audience findAudienceById(String id) {
        return audienceDao.findById(id).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));
    }

    @Override
    public Audience findAudienceByNumber(Integer number) {
        return audienceDao.findByNumber(number).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));
    }

    @Override
    public List<Audience> showAllAudiences(Page page) {
        return audienceDao.findAll(page);
    }

    @Override
    public void deleteAudience(String id) {
        if (!audienceDao.findById(id).isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        audienceDao.deleteById(id);
    }

    @Override
    public void editAudience(Audience audience) {
        Optional<Audience> audienceDb = audienceDao.findById(audience.getId());
        if (!audienceDb.isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        Audience audienceToEdit = audienceDb.get();
        if (isNumberChanged(audience, audienceToEdit)) {
            if (audienceDao.findByNumber(audience.getNumber()).isPresent()) {
                throw new EntityAlreadyExistException("Specified number already exists");
            }
        }
        validator.validate(audience);
        audienceDao.update(audience);
    }

    protected boolean isNumberChanged(Audience audience, Audience audienceToEdit) {
        return !Objects.equals(audienceToEdit.getNumber(), audience.getNumber());
    }
}
