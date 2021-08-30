package ua.com.foxminded.university.spring.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.spring.dao.AudienceDao;
import ua.com.foxminded.university.spring.dao.mapper.AudienceMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AudienceDaoImpl extends AbstractCrudDaoImpl<Audience> implements AudienceDao {

    protected final AudienceMapper audienceMapper;

    private static final String SAVE_QUERY =
            "INSERT INTO audience (audience_id, audience_number, audience_floor) VALUES (?,?,?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM audience WHERE audience_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM audience";
    private static final String FIND_ALL_PAGED_QUERY = "SELECT * FROM audience LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY =
            "UPDATE audience SET audience_number = ?, audience_floor = ? WHERE audience_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM audience WHERE audience_id = ?";

    public AudienceDaoImpl(JdbcTemplate jdbcTemplate, AudienceMapper audienceMapper) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGED_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
        this.audienceMapper = audienceMapper;
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Audience entity)
            throws SQLException {
        preparedStatement.setString(1, entity.getId());
        preparedStatement.setInt(2, entity.getNumber());
        preparedStatement.setInt(3, entity.getFloor());
    }

    @Override
    protected void insertAll(PreparedStatement preparedStatement, int i, List<Audience> entities)
            throws SQLException {
        preparedStatement.setString(1, entities.get(i).getId());
        preparedStatement.setInt(2, entities.get(i).getNumber());
        preparedStatement.setInt(3, entities.get(i).getFloor());
    }

    @Override
    protected RowMapper<Audience> rowMapper() {
        return audienceMapper;
    }

    @Override
    protected void update(PreparedStatement preparedStatement, Audience entity)
            throws SQLException {
        preparedStatement.setInt(1, entity.getNumber());
        preparedStatement.setInt(2, entity.getFloor());
        preparedStatement.setString(3, entity.getId());
    }
}
