package ua.com.foxminded.university.spring.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.spring.dao.FacultyDao;
import ua.com.foxminded.university.spring.dao.mapper.FacultyMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class FacultyDaoImpl extends AbstractCrudDaoImpl<Faculty> implements FacultyDao {

    private static final String SAVE_QUERY = "INSERT INTO faculty (faculty_id, faculty_name) VALUES (?,?)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT faculty.faculty_id, faculty.faculty_name, \"group\".group_id, "
                    + "\"group\".group_name, \"group\".group_course,  student.student_id, student.student_email, "
                    + "student.student_password, student.student_name, student.student_birthday, "
                    + "FROM faculty LEFT JOIN faculty_group ON faculty.faculty_id = faculty_group.faculty "
                    + "LEFT JOIN \"group\" ON faculty_group.\"group\" = \"group\".group_id "
                    + "LEFT JOIN group_student ON \"group\".group_id = group_student.\"group\" "
                    + "LEFT JOIN student ON group_student.student = student.student_id WHERE faculty_id = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT faculty.faculty_id, faculty.faculty_name, \"group\".group_id, "
                    + "\"group\".group_name, \"group\".group_course, student.student_id, student.student_email, "
                    + "student.student_password, student.student_name, student.student_birthday, "
                    + "FROM faculty LEFT JOIN faculty_group ON faculty.faculty_id = faculty_group.faculty "
                    + "LEFT JOIN \"group\" ON faculty_group.\"group\" = \"group\".group_id "
                    + "LEFT JOIN group_student ON \"group\".group_id = group_student.\"group\" "
                    + "LEFT JOIN student ON group_student.student = student.student_id ORDER BY faculty_id";
    private static final String FIND_ALL_PAGED_QUERY =
            "SELECT faculty.faculty_id, faculty.faculty_name, \"group\".group_id, "
                    + "\"group\".group_name, \"group\".group_course, student.student_id, student.student_email, "
                    + "student.student_password, student.student_name, student.student_birthday, "
                    + "FROM faculty LEFT JOIN faculty_group ON faculty.faculty_id = faculty_group.faculty "
                    + "LEFT JOIN \"group\" ON faculty_group.\"group\" = \"group\".group_id "
                    + "LEFT JOIN group_student ON \"group\".group_id = group_student.\"group\" "
                    + "LEFT JOIN student ON group_student.student = student.student_id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE faculty SET faculty_name = ? WHERE faculty_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM faculty WHERE faculty_id = ?";

    private static final String ADD_GROUP_TO_FACULTY_QUERY =
            "INSERT INTO faculty_group (faculty, \"group\") VALUES (?,?)";
    private static final String DELETE_GROUP_FROM_FACULTY_QUERY =
            "DELETE FROM faculty_group WHERE faculty = ? AND \"group\" = ?";

    private static final String FIND_BY_NAME_QUERY = "SELECT faculty.faculty_id, faculty.faculty_name, \"group\".group_id, "
            + "\"group\".group_name, \"group\".group_course,  student.student_id, student.student_email, "
            + "student.student_password, student.student_name, student.student_birthday, "
            + "FROM faculty LEFT JOIN faculty_group ON faculty.faculty_id = faculty_group.faculty "
            + "LEFT JOIN \"group\" ON faculty_group.\"group\" = \"group\".group_id "
            + "LEFT JOIN group_student ON \"group\".group_id = group_student.\"group\" "
            + "LEFT JOIN student ON group_student.student = student.student_id WHERE faculty_name = ?";

    protected final FacultyMapper facultyMapper;

    public FacultyDaoImpl(JdbcTemplate jdbcTemplate, FacultyMapper facultyMapper) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGED_QUERY, UPDATE_QUERY,
                DELETE_BY_ID_QUERY);
        this.facultyMapper = facultyMapper;
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Faculty entity) throws SQLException {
        preparedStatement.setString(1, entity.getId());
        preparedStatement.setString(2, entity.getName());
    }

    @Override
    protected void insertAll(PreparedStatement preparedStatement, int i, List<Faculty> entities)
            throws SQLException {
        preparedStatement.setString(1, entities.get(i).getId());
        preparedStatement.setString(2, entities.get(i).getName());
    }

    @Override
    protected RowMapper<Faculty> rowMapper() {
        return facultyMapper;
    }

    @Override
    protected void update(PreparedStatement preparedStatement, Faculty entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getId());
    }

    @Override
    public void addGroupToFaculty(String facultyId, String groupId) {
        jdbcTemplate.update(ADD_GROUP_TO_FACULTY_QUERY, ps -> {
            ps.setString(1, facultyId);
            ps.setString(2, groupId);
        });
    }

    @Override
    public void addAllGroupsToFaculty(Faculty faculty) {
        jdbcTemplate.batchUpdate(ADD_GROUP_TO_FACULTY_QUERY, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, faculty.getId());
                ps.setString(2, faculty.getGroups().get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return faculty.getGroups().size();
            }
        });
    }

    @Override
    public void removeGroupFromFaculty(String facultyId, String groupId) {
        jdbcTemplate.update(DELETE_GROUP_FROM_FACULTY_QUERY, ps -> {
            ps.setString(1, facultyId);
            ps.setString(2, groupId);
        });
    }

    @Override
    public Optional<Faculty> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, rowMapper(), name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
