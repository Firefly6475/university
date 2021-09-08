package ua.com.foxminded.university.spring.dao;

import ua.com.foxminded.university.model.Audience;

import java.util.Optional;

public interface AudienceDao extends CrudDao<Audience> {
    Optional<Audience> findByNumber(Integer number);
}
