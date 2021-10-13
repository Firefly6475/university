package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Audience;

import java.util.List;

public interface AudienceService {
    void addAudience(Audience audience);

    Audience findAudienceById(String id);

    Audience findAudienceByNumber(Integer number);

    List<Audience> showAllAudiences();

    List<Audience> showAllAudiences(Integer pageNumber);

    void deleteAudience(String id);

    void editAudience(Audience audience);
}
