package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.spring.dao.Page;

import java.util.List;

public interface AudienceService {
    void addAudience(Audience audience);

    Audience findAudienceById(String id);

    Audience findAudienceByNumber(Integer number);

    List<Audience> showAllAudiences(Page page);

    void deleteAudience(String id);

    void editAudience(Audience audience);
}
