package ua.com.foxminded.university.spring.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.spring.dao.TeacherDao;
import ua.com.foxminded.university.spring.dao.mapper.TeacherMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TeacherDaoImpl extends AbstractCrudDaoImpl<Teacher> implements TeacherDao {

    protected final TeacherMapper teacherMapper;

    private static final String SAVE_QUERY =
            "INSERT INTO teacher (teacher_id, teacher_email, teacher_password, teacher_name, teacher_birthday) VALUES (?,?,?,?,?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * from teacher WHERE teacher_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * from teacher";
    private static final String FIND_ALL_PAGED_QUERY = "SELECT * from teacher LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY =
            "UPDATE teacher SET teacher_email = ?, teacher_password = ?, teacher_name = ?, teacher_birthday = ? WHERE teacher_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM teacher WHERE teacher_id = ?";

    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM teacher WHERE teacher_email = ?";

    public TeacherDaoImpl(JdbcTemplate jdbcTemplate, TeacherMapper teacherMapper) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGED_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
        this.teacherMapper = teacherMapper;
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Teacher entity) throws SQLException {
        preparedStatement.setString(1, entity.getId());
        preparedStatement.setString(2, entity.getEmail());
        preparedStatement.setString(3, entity.getPassword());
        preparedStatement.setString(4, entity.getName());
        preparedStatement.setObject(5, entity.getBirthday());
    }

    @Override
    protected void insertAll(PreparedStatement preparedStatement, int i, List<Teacher> entities)
            throws SQLException {
        preparedStatement.setString(1, entities.get(i).getId());
        preparedStatement.setString(2, entities.get(i).getEmail());
        preparedStatement.setString(3, entities.get(i).getPassword());
        preparedStatement.setString(4, entities.get(i).getName());
        preparedStatement.setObject(5, entities.get(i).getBirthday());
    }

    @Override
    protected RowMapper<Teacher> rowMapper() {
        return teacherMapper;
    }

    @Override
    protected void update(PreparedStatement preparedStatement, Teacher entity) throws SQLException {
        preparedStatement.setString(1, entity.getEmail());
        preparedStatement.setString(2, entity.getPassword());
        preparedStatement.setString(3, entity.getName());
        preparedStatement.setObject(4, entity.getBirthday());
        preparedStatement.setString(5, entity.getId());
    }

    @Override
    public Optional<Teacher> findByEmail(String email) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_EMAIL_QUERY, rowMapper(), email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
