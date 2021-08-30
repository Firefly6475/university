package ua.com.foxminded.university.spring.dao.impl;

import lombok.NonNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.spring.dao.DisciplineDao;
import ua.com.foxminded.university.spring.dao.mapper.DisciplineMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DisciplineDaoImpl extends AbstractCrudDaoImpl<Discipline> implements DisciplineDao {

    private static final String SAVE_QUERY = "INSERT INTO discipline (discipline_id, discipline_name) VALUES (?,?)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT discipline.discipline_id, discipline.discipline_name, "
                    + "teacher.teacher_id, teacher.teacher_name, teacher.teacher_birthday, "
                    + "teacher.teacher_salary FROM discipline "
                    + "INNER JOIN discipline_teacher ON discipline.discipline_id = discipline_teacher.discipline "
                    + "INNER JOIN teacher ON discipline_teacher.teacher = teacher.teacher_id WHERE discipline.discipline_id = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT discipline.discipline_id, discipline.discipline_name, "
                    + "teacher.teacher_id, teacher.teacher_name, teacher.teacher_birthday, "
                    + "teacher.teacher_salary FROM discipline "
                    + "INNER JOIN discipline_teacher ON discipline.discipline_id = discipline_teacher.discipline "
                    + "INNER JOIN teacher ON discipline_teacher.teacher = teacher.teacher_id ORDER BY discipline_id";
    private static final String FIND_ALL_PAGED_QUERY =
            "SELECT discipline.discipline_id, discipline.discipline_name, "
                    + "teacher.teacher_id, teacher.teacher_name, teacher.teacher_birthday, "
                    + "teacher.teacher_salary FROM discipline "
                    + "INNER JOIN discipline_teacher ON discipline.discipline_id = discipline_teacher.discipline "
                    + "INNER JOIN teacher ON discipline_teacher.teacher = teacher.teacher_id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE discipline SET discipline_name = ? WHERE discipline_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM discipline WHERE discipline_id = ?";

    private static final String ADD_TEACHER_TO_DISCIPLINE_QUERY =
            "INSERT INTO discipline_teacher (discipline, teacher) VALUES (?,?)";
    private static final String DELETE_TEACHER_FROM_DISCIPLINE_QUERY =
            "DELETE FROM discipline_teacher WHERE discipline = ? AND teacher = ?";

    protected final DisciplineMapper disciplineMapper;

    public DisciplineDaoImpl(JdbcTemplate jdbcTemplate, DisciplineMapper disciplineMapper) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGED_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
        this.disciplineMapper = disciplineMapper;
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Discipline entity)
            throws SQLException {
        preparedStatement.setString(1, entity.getId());
        preparedStatement.setString(2, entity.getName());
    }

    @Override
    protected void insertAll(PreparedStatement preparedStatement, int i, List<Discipline> entities)
            throws SQLException {
        preparedStatement.setString(1, entities.get(i).getId());
        preparedStatement.setString(2, entities.get(i).getName());
    }

    @Override
    protected RowMapper<Discipline> rowMapper() {
        return disciplineMapper;
    }

    @Override
    protected void update(PreparedStatement preparedStatement, Discipline entity)
            throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getId());
    }

    @Override
    public void addTeacherToDiscipline(String disciplineId, String teacherId) {
        jdbcTemplate.update(ADD_TEACHER_TO_DISCIPLINE_QUERY, ps -> {
            ps.setString(1, disciplineId);
            ps.setString(2, teacherId);
        });
    }

    @Override
    public void addAllTeachersToDiscipline(Discipline discipline) {
        jdbcTemplate.batchUpdate(ADD_TEACHER_TO_DISCIPLINE_QUERY,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, discipline.getId());
                        ps.setString(2, discipline.getTeachers().get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return discipline.getTeachers().size();
                    }
                });
    }

    @Override
    public void removeTeacherFromDiscipline(String disciplineId, String teacherId) {
        jdbcTemplate.update(DELETE_TEACHER_FROM_DISCIPLINE_QUERY, ps -> {
            ps.setString(1, disciplineId);
            ps.setString(2, teacherId);
        });
    }
}
