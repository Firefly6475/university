package ua.com.foxminded.university.spring.dao.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.spring.dao.mapper.DepartmentMapper;
import ua.com.foxminded.university.spring.dao.DepartmentDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DepartmentDaoImpl extends AbstractCrudDaoImpl<Department>
        implements DepartmentDao {

    private static final String SAVE_QUERY = "INSERT INTO department (department_id, department_name) VALUES (?,?)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT department.department_id, department.department_name, teacher.teacher_id , "
                    + "teacher.teacher_name, teacher.teacher_birthday, teacher.teacher_salary "
                    + "FROM department INNER JOIN department_teacher ON department.department_id = department_teacher.department "
                    + "INNER JOIN teacher ON department_teacher.teacher = teacher.teacher_id WHERE department.department_id = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT department.department_id, department.department_name, teacher.teacher_id , "
                    + "teacher.teacher_name, teacher.teacher_birthday, teacher.teacher_salary "
                    + "FROM department INNER JOIN department_teacher ON department.department_id = department_teacher.department "
                    + "INNER JOIN teacher ON department_teacher.teacher = teacher.teacher_id ORDER BY department_id";
    private static final String FIND_ALL_PAGED_QUERY =
            "SELECT department.department_id, department.department_name, teacher.teacher_id , "
                    + "teacher.teacher_name, teacher.teacher_birthday, teacher.teacher_salary "
                    + "FROM department INNER JOIN department_teacher ON department.department_id = department_teacher.department "
                    + "INNER JOIN teacher ON department_teacher.teacher = teacher.teacher_id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE department SET department_name = ? WHERE department_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM department WHERE department_id = ?";

    private static final String ADD_TEACHER_TO_DEPARTMENT_QUERY =
            "INSERT INTO department_teacher (department, teacher) VALUES (?,?)";
    private static final String DELETE_TEACHER_FROM_DEPARTMENT_QUERY =
            "DELETE FROM department_teacher WHERE department = ? AND teacher = ?";

    protected final DepartmentMapper departmentMapper;

    public DepartmentDaoImpl(JdbcTemplate jdbcTemplate, DepartmentMapper departmentMapper) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGED_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
        this.departmentMapper = departmentMapper;
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Department entity)
            throws SQLException {
        preparedStatement.setString(1, entity.getId());
        preparedStatement.setString(2, entity.getName());
    }

    @Override
    protected void insertAll(PreparedStatement preparedStatement, int i, List<Department> entities)
            throws SQLException {
        preparedStatement.setString(1, entities.get(i).getId());
        preparedStatement.setString(2, entities.get(i).getName());
    }

    @Override
    protected RowMapper<Department> rowMapper() {
        return departmentMapper;
    }

    @Override
    protected void update(PreparedStatement preparedStatement, Department entity)
            throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getId());
    }

    @Override
    public void addTeacherToDepartment(String departmentId, String teacherId) {
        jdbcTemplate.update(ADD_TEACHER_TO_DEPARTMENT_QUERY, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, departmentId);
                ps.setString(2, teacherId);
            }
        });
    }

    @Override
    public void addAllTeachersToDepartment(Department department) {
        jdbcTemplate.batchUpdate(ADD_TEACHER_TO_DEPARTMENT_QUERY,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, department.getId());
                        ps.setString(2, department.getTeachers().get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return department.getTeachers().size();
                    }
                });
    }

    @Override
    public void removeTeacherFromDepartment(String departmentId, String teacherId) {
        jdbcTemplate.update(DELETE_TEACHER_FROM_DEPARTMENT_QUERY, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, departmentId);
                ps.setString(2, teacherId);
            }
        });
    }
}
