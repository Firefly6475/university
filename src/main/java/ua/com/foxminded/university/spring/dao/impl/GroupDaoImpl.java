package ua.com.foxminded.university.spring.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.spring.dao.GroupDao;
import ua.com.foxminded.university.spring.dao.mapper.GroupMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class GroupDaoImpl extends AbstractCrudDaoImpl<Group> implements GroupDao {

    private static final String SAVE_QUERY = "INSERT INTO \"group\" (group_id, group_name, group_course) VALUES (?,?,?)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT \"group\".group_id, \"group\".group_name, \"group\".group_course, student.student_id, "
                    + "student.student_email, student_password, student.student_name, student.student_birthday "
                    + "FROM \"group\" LEFT JOIN group_student ON \"group\".group_id = group_student.\"group\" "
                    + "LEFT JOIN student ON group_student.student = student.student_id WHERE group_id = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT \"group\".group_id, \"group\".group_name, \"group\".group_course, student.student_id, "
                    + "student.student_email, student_password, student.student_name, student.student_birthday "
                    + "FROM \"group\" LEFT JOIN group_student ON \"group\".group_id = group_student.\"group\" "
                    + "LEFT JOIN student ON group_student.student = student.student_id ORDER BY group_id";
    private static final String FIND_ALL_PAGED_QUERY =
            "SELECT \"group\".group_id, \"group\".group_name, \"group\".group_course, student.student_id, "
                    + "student.student_email, student_password, student.student_name, student.student_birthday "
                    + "FROM \"group\" LEFT JOIN group_student ON \"group\".group_id = group_student.group "
                    + "LEFT JOIN student ON group_student.student = student.student_id ORDER BY group_id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE \"group\" SET group_name = ?, group_course = ? WHERE group_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM \"group\" WHERE group_id = ?";

    private static final String ADD_STUDENT_TO_GROUP_QUERY =
            "INSERT INTO group_student (\"group\", student) VALUES (?,?)";
    private static final String DELETE_STUDENT_FROM_GROUP_QUERY =
            "DELETE FROM group_student WHERE \"group\" = ? AND student = ?";
    private static final String FIND_BY_NAME_QUERY =
            "SELECT \"group\".group_id, \"group\".group_name, \"group\".group_course, student.student_id, "
            + "student.student_email, student_password, student.student_name, student.student_birthday "
            + "FROM \"group\" LEFT JOIN group_student ON \"group\".group_id = group_student.\"group\" "
            + "LEFT JOIN student ON group_student.student = student.student_id WHERE group_name = ?";

    protected final GroupMapper groupMapper;

    public GroupDaoImpl(JdbcTemplate jdbcTemplate, GroupMapper groupMapper) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGED_QUERY, UPDATE_QUERY,
                DELETE_BY_ID_QUERY);
        this.groupMapper = groupMapper;
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Group entity) throws SQLException {
        preparedStatement.setString(1, entity.getId());
        preparedStatement.setString(2, entity.getName());
        preparedStatement.setInt(3, entity.getCourse());
    }

    @Override
    protected void insertAll(PreparedStatement preparedStatement, int i, List<Group> entities)
            throws SQLException {
        preparedStatement.setString(1, entities.get(i).getId());
        preparedStatement.setString(2, entities.get(i).getName());
        preparedStatement.setInt(3, entities.get(i).getCourse());
    }

    @Override
    protected RowMapper<Group> rowMapper() {
        return groupMapper;
    }

    @Override
    protected void update(PreparedStatement preparedStatement, Group entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setInt(2, entity.getCourse());
        preparedStatement.setString(3, entity.getId());
    }

    @Override
    public void addStudentToGroup(String groupId, String studentId) {
        jdbcTemplate.update(ADD_STUDENT_TO_GROUP_QUERY, ps -> {
            ps.setString(1, groupId);
            ps.setString(2, studentId);
        });
    }

    @Override
    public void addAllStudentsToGroup(Group group) {
        jdbcTemplate.batchUpdate(ADD_STUDENT_TO_GROUP_QUERY, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, group.getId());
                ps.setString(2, group.getStudents().get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return group.getStudents().size();
            }
        });
    }

    @Override
    public void removeStudentFromGroup(String groupId, String studentId) {
        jdbcTemplate.update(DELETE_STUDENT_FROM_GROUP_QUERY, ps -> {
            ps.setString(1, groupId);
            ps.setString(2, studentId);
        });
    }

    @Override
    public Optional<Group> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, rowMapper(), name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
