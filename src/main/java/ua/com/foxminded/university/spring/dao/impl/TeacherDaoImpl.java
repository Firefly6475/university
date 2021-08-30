package ua.com.foxminded.university.spring.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.spring.dao.TeacherDao;
import ua.com.foxminded.university.spring.dao.mapper.TeacherMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TeacherDaoImpl extends AbstractCrudDaoImpl<Teacher> implements TeacherDao {

    protected final TeacherMapper teacherMapper;

    private static final String SAVE_QUERY =
            "INSERT INTO teacher (teacher_id, teacher_name, teacher_birthday, teacher_salary) VALUES (?,?,?,?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * from teacher WHERE teacher_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * from teacher";
    private static final String FIND_ALL_PAGED_QUERY = "SELECT * from teacher LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY =
            "UPDATE teacher SET teacher_name = ?, teacher_birthday = ?, teacher_salary = ? WHERE teacher_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM teacher WHERE teacher_id = ?";

    public TeacherDaoImpl(JdbcTemplate jdbcTemplate, TeacherMapper teacherMapper) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGED_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
        this.teacherMapper = teacherMapper;
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Teacher entity) throws SQLException {
        preparedStatement.setString(1, entity.getId());
        preparedStatement.setString(2, entity.getName());
        preparedStatement.setObject(3, entity.getBirthday());
        preparedStatement.setInt(4, entity.getSalary());
    }

    @Override
    protected void insertAll(PreparedStatement preparedStatement, int i, List<Teacher> entities)
            throws SQLException {
        preparedStatement.setString(1, entities.get(i).getId());
        preparedStatement.setString(2, entities.get(i).getName());
        preparedStatement.setObject(3, entities.get(i).getBirthday());
        preparedStatement.setInt(4, entities.get(i).getSalary());
    }

    @Override
    protected RowMapper<Teacher> rowMapper() {
        return teacherMapper;
    }

    @Override
    protected void update(PreparedStatement preparedStatement, Teacher entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setObject(2, entity.getBirthday());
        preparedStatement.setInt(3, entity.getSalary());
        preparedStatement.setString(4, entity.getId());
    }
}
