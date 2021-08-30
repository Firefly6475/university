package ua.com.foxminded.university.spring.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.spring.dao.StudentDao;
import ua.com.foxminded.university.spring.dao.mapper.StudentMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StudentDaoImpl extends AbstractCrudDaoImpl<Student> implements StudentDao {

    protected final StudentMapper studentMapper;

    private static final String SAVE_QUERY =
            "INSERT INTO student (student_id, student_email, student_password, student_name, student_birthday) VALUES (?,?,?,?,?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM student WHERE student_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM student ORDER BY student_id ASC ";
    private static final String FIND_ALL_PAGED_QUERY = "SELECT * FROM student LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY =
            "UPDATE student SET student_email = ?, student_password = ?, student_name = ?, student_birthday = ? WHERE student_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM student WHERE student_id = ?";

    public StudentDaoImpl(JdbcTemplate jdbcTemplate, StudentMapper studentMapper) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGED_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
        this.studentMapper = studentMapper;
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Student entity) throws SQLException {
        preparedStatement.setString(1, entity.getId());
        preparedStatement.setString(2, entity.getEmail());
        preparedStatement.setString(3, entity.getPassword());
        preparedStatement.setString(4, entity.getName());
        preparedStatement.setObject(5, entity.getBirthday());
    }

    @Override
    protected void insertAll(PreparedStatement preparedStatement, int i, List<Student> entities)
            throws SQLException {
        preparedStatement.setString(1, entities.get(i).getId());
        preparedStatement.setString(2, entities.get(i).getEmail());
        preparedStatement.setString(3, entities.get(i).getPassword());
        preparedStatement.setString(4, entities.get(i).getName());
        preparedStatement.setObject(5, entities.get(i).getBirthday());
    }

    @Override
    protected RowMapper<Student> rowMapper() {
        return studentMapper;
    }

    @Override
    protected void update(PreparedStatement preparedStatement, Student entity) throws SQLException {
        preparedStatement.setString(1, entity.getEmail());
        preparedStatement.setString(2, entity.getPassword());
        preparedStatement.setString(3, entity.getName());
        preparedStatement.setObject(4, entity.getBirthday());
        preparedStatement.setString(5, entity.getId());
    }
}
