package ua.com.foxminded.university.spring.dao.mapper;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.spring.dao.mapper.generic.AbstractCollectingRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AudienceMapper extends AbstractCollectingRowMapper<Audience> {
    @Override
    protected Audience mapRow(ResultSet rs, Audience partialResult, int rowNum) throws SQLException {
        return Audience.builder()
                    .withId(rs.getString("audience_id"))
                    .withNumber(rs.getInt("audience_number"))
                    .withFloor(rs.getInt("audience_floor"))
                    .build();
    }

    @Override
    protected boolean isRelated(ResultSet rs, Audience partialResult) throws SQLException {
        return partialResult.getId().equals((rs.getString("audience_id")));
    }
}
