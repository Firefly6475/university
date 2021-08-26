package ua.com.foxminded.university.spring.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.spring.dao.mapper.FacultyMapper;
import ua.com.foxminded.university.spring.dao.FacultyDao;

@Repository
public class FacultyDaoImpl extends AbstractCrudDaoImpl<Faculty> implements FacultyDao {
    
    protected final FacultyMapper facultyMapper;
    
    private static final String SAVE_QUERY = "INSERT INTO faculty (faculty_id, faculty_name) VALUES (?,?)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT faculty.faculty_id, faculty.faculty_name, \"group\".group_id, "
                    + "\"group\".group_name, student.student_id, student.student_name, "
                    + "student.student_birthday, student.student_course "
                    + "FROM faculty INNER JOIN faculty_group ON faculty.faculty_id = faculty_group.faculty "
                    + "INNER JOIN \"group\" ON faculty_group.\"group\" = \"group\".group_id "
                    + "INNER JOIN group_student ON \"group\".group_id = group_student.\"group\" "
                    + "INNER JOIN student ON group_student.student = student.student_id WHERE faculty_id = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT faculty.faculty_id, faculty.faculty_name, \"group\".group_id, "
                    + "\"group\".group_name, student.student_id, student.student_name, "
                    + "student.student_birthday, student.student_course "
                    + "FROM faculty INNER JOIN faculty_group ON faculty.faculty_id = faculty_group.faculty "
                    + "INNER JOIN \"group\" ON faculty_group.\"group\" = \"group\".group_id "
                    + "INNER JOIN group_student ON \"group\".group_id = group_student.\"group\" "
                    + "INNER JOIN student ON group_student.student = student.student_id ORDER BY faculty_id";
    private static final String FIND_ALL_PAGED_QUERY =
            "SELECT faculty.faculty_id, faculty.faculty_name, \"group\".group_id, "
                    + "\"group\".group_name, student.student_id, student.student_name, "
                    + "student.student_birthday, student.student_course "
                    + "FROM faculty INNER JOIN faculty_group ON faculty.faculty_id = faculty_group.faculty "
                    + "INNER JOIN \"group\" ON faculty_group.\"group\" = \"group\".group_id "
                    + "INNER JOIN group_student ON \"group\".group_id = group_student.\"group\" "
                    + "INNER JOIN student ON group_student.student = student.student_id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE faculty SET faculty_name = ? WHERE faculty_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM faculty WHERE faculty_id = ?";
    
    private static final String ADD_GROUP_TO_FACULTY_QUERY =
            "INSERT INTO faculty_group (faculty, \"group\") VALUES (?,?)";
    private static final String DELETE_GROUP_FROM_FACULTY_QUERY =
            "DELETE FROM faculty_group WHERE faculty = ? AND \"group\" = ?";
    
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
        jdbcTemplate.update(ADD_GROUP_TO_FACULTY_QUERY, new PreparedStatementSetter() {
            
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, facultyId);
                ps.setString(2, groupId);
            }
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
        jdbcTemplate.update(DELETE_GROUP_FROM_FACULTY_QUERY, new PreparedStatementSetter() {
            
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, facultyId);
                ps.setString(2, groupId);
            }
        });
    }
}
