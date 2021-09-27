package ua.com.foxminded.university.spring.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.spring.dao.LessonDao;
import ua.com.foxminded.university.spring.dao.mapper.LessonMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LessonDaoImpl extends AbstractCrudDaoImpl<Lesson> implements LessonDao {

    private static final String SAVE_QUERY =
            "INSERT INTO lesson (lesson_id, discipline, audience, lesson_type, \"date\", "
                    + "time_start, time_end, \"group\", teacher) VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT lesson.lesson_id, discipline.discipline_id, discipline.discipline_name, " +
                    "teacher.teacher_id, teacher.teacher_email, teacher.teacher_password, teacher.teacher_name, teacher.teacher_birthday, " +
                    "audience.audience_id, audience.audience_number, audience.audience_floor, " +
                    "lesson.lesson_type, lesson.\"date\", lesson.time_start, " +
                    "lesson.time_end, \"group\".group_id, \"group\".group_name, \"group\".group_course, " +
                    "student.student_id, student.student_email, student.student_password, student.student_name, student.student_birthday, " +
                    "teacher_table.teacher_id AS lesson_teacher_id, teacher_table.teacher_email AS lesson_teacher_email, " +
                    "teacher_table.teacher_password AS lesson_teacher_password, teacher_table.teacher_name AS lesson_teacher_name, " +
                    "teacher_table.teacher_birthday AS lesson_teacher_birthday, " +
                    "FROM lesson LEFT JOIN discipline ON lesson.discipline = discipline.discipline_id " +
                    "LEFT JOIN discipline_teacher ON discipline.discipline_id = discipline_teacher.discipline " +
                    "LEFT JOIN teacher ON discipline_teacher.teacher = teacher.teacher_id " +
                    "LEFT JOIN audience ON lesson.audience = audience.audience_id " +
                    "LEFT JOIN \"group\" ON lesson.\"group\" = \"group\".group_id " +
                    "LEFT JOIN group_student ON \"group\".group_id = group_student.\"group\" " +
                    "LEFT JOIN student ON group_student.student = student.student_id " +
                    "LEFT JOIN (select * from teacher) AS teacher_table ON lesson.teacher = teacher_table.teacher_id " +
                    "WHERE lesson_id = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT lesson.lesson_id, discipline.discipline_id, discipline.discipline_name, " +
                    "teacher.teacher_id, teacher.teacher_email, teacher.teacher_password, teacher.teacher_name, teacher.teacher_birthday, " +
                    "audience.audience_id, audience.audience_number, audience.audience_floor, " +
                    "lesson.lesson_type, lesson.\"date\", lesson.time_start, " +
                    "lesson.time_end, \"group\".group_id, \"group\".group_name, \"group\".group_course, " +
                    "student.student_id, student.student_email, student.student_password, student.student_name, student.student_birthday, " +
                    "teacher_table.teacher_id AS lesson_teacher_id, teacher_table.teacher_email AS lesson_teacher_email, " +
                    "teacher_table.teacher_password AS lesson_teacher_password, teacher_table.teacher_name AS lesson_teacher_name, " +
                    "teacher_table.teacher_birthday AS lesson_teacher_birthday " +
                    "FROM lesson LEFT JOIN discipline ON lesson.discipline = discipline.discipline_id " +
                    "LEFT JOIN discipline_teacher ON discipline.discipline_id = discipline_teacher.discipline " +
                    "LEFT JOIN teacher ON discipline_teacher.teacher = teacher.teacher_id " +
                    "LEFT JOIN audience ON lesson.audience = audience.audience_id " +
                    "LEFT JOIN \"group\" ON lesson.\"group\" = \"group\".group_id " +
                    "LEFT JOIN group_student ON \"group\".group_id = group_student.\"group\" " +
                    "LEFT JOIN student ON group_student.student = student.student_id " +
                    "LEFT JOIN (select * from teacher) AS teacher_table ON lesson.teacher = teacher_table.teacher_id " +
                    "ORDER BY lesson_id";
    private static final String FIND_ALL_PAGED_QUERY =
            "SELECT lesson.lesson_id, discipline.discipline_id, discipline.discipline_name, " +
                    "teacher.teacher_id, teacher.teacher_email, teacher.teacher_password, teacher.teacher_name, teacher.teacher_birthday, " +
                    "audience.audience_id, audience.audience_number, audience.audience_floor, " +
                    "lesson.lesson_type, lesson.\"date\", lesson.time_start, " +
                    "lesson.time_end, \"group\".group_id, \"group\".group_name, \"group\".group_course, " +
                    "student.student_id, student.student_email, student.student_password, student.student_name, student.student_birthday, " +
                    "teacher_table.teacher_id AS lesson_teacher_id, teacher_table.teacher_email AS lesson_teacher_email, " +
                    "teacher_table.teacher_password AS lesson_teacher_password, teacher_table.teacher_name AS lesson_teacher_name, " +
                    "teacher_table.teacher_birthday AS lesson_teacher_birthday " +
                    "FROM lesson LEFT JOIN discipline ON lesson.discipline = discipline.discipline_id " +
                    "LEFT JOIN discipline_teacher ON discipline.discipline_id = discipline_teacher.discipline " +
                    "LEFT JOIN teacher ON discipline_teacher.teacher = teacher.teacher_id " +
                    "LEFT JOIN audience ON lesson.audience = audience.audience_id " +
                    "LEFT JOIN \"group\" ON lesson.\"group\" = \"group\".group_id " +
                    "LEFT JOIN group_student ON \"group\".group_id = group_student.\"group\" " +
                    "LEFT JOIN student ON group_student.student = student.student_id " +
                    "LEFT JOIN (select * from teacher) AS teacher_table ON lesson.teacher = teacher_table.teacher_id " +
                    "LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY =
            "UPDATE lesson SET discipline = ?, audience = ?, lesson_type = ?, \"date\" = ?, "
                    + "time_start = ?, time_end = ?, \"group\" = ?, teacher = ? WHERE lesson_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM lesson WHERE lesson_id = ?";
    private static final String FIND_BY_DISCIPLINE_NAME_QUERY = "SELECT lesson.lesson_id, discipline.discipline_id, discipline.discipline_name, " +
            "teacher.teacher_id, teacher.teacher_email, teacher.teacher_password, teacher.teacher_name, teacher.teacher_birthday, " +
            "audience.audience_id, audience.audience_number, audience.audience_floor, " +
            "lesson.lesson_type, lesson.\"date\", lesson.time_start, " +
            "lesson.time_end, \"group\".group_id, \"group\".group_name, \"group\".group_course, " +
            "student.student_id, student.student_email, student.student_password, student.student_name, student.student_birthday, " +
            "teacher_table.teacher_id AS lesson_teacher_id, teacher_table.teacher_email AS lesson_teacher_email, " +
            "teacher_table.teacher_password AS lesson_teacher_password, teacher_table.teacher_name AS lesson_teacher_name, " +
            "teacher_table.teacher_birthday AS lesson_teacher_birthday, " +
            "FROM lesson LEFT JOIN discipline ON lesson.discipline = discipline.discipline_id " +
            "LEFT JOIN discipline_teacher ON discipline.discipline_id = discipline_teacher.discipline " +
            "LEFT JOIN teacher ON discipline_teacher.teacher = teacher.teacher_id " +
            "LEFT JOIN audience ON lesson.audience = audience.audience_id " +
            "LEFT JOIN \"group\" ON lesson.\"group\" = \"group\".group_id " +
            "LEFT JOIN group_student ON \"group\".group_id = group_student.\"group\" " +
            "LEFT JOIN student ON group_student.student = student.student_id " +
            "LEFT JOIN (select * from teacher) AS teacher_table ON lesson.teacher = teacher_table.teacher_id " +
            "WHERE discipline.discipline_name = ?";
    protected final LessonMapper lessonMapper;

    public LessonDaoImpl(JdbcTemplate jdbcTemplate, LessonMapper lessonMapper) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGED_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
        this.lessonMapper = lessonMapper;
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Lesson entity) throws SQLException {
        preparedStatement.setString(1, entity.getId());
        preparedStatement.setString(2, entity.getDiscipline().getId());
        preparedStatement.setString(3, entity.getAudience().getId());
        preparedStatement.setString(4, entity.getLessonType().toString());
        preparedStatement.setObject(5, entity.getDate());
        preparedStatement.setObject(6, entity.getTimeStart());
        preparedStatement.setObject(7, entity.getTimeEnd());
        preparedStatement.setString(8, entity.getGroup().getId());
        preparedStatement.setString(9, entity.getTeacher().getId());
    }

    @Override
    protected void insertAll(PreparedStatement preparedStatement, int i, List<Lesson> entities)
            throws SQLException {
        preparedStatement.setString(1, entities.get(i).getId());
        preparedStatement.setString(2, entities.get(i).getDiscipline().getId());
        preparedStatement.setString(3, entities.get(i).getAudience().getId());
        preparedStatement.setString(4, entities.get(i).getLessonType().toString());
        preparedStatement.setObject(5, entities.get(i).getDate());
        preparedStatement.setObject(6, entities.get(i).getTimeStart());
        preparedStatement.setObject(7, entities.get(i).getTimeEnd());
        preparedStatement.setString(8, entities.get(i).getGroup().getId());
        preparedStatement.setString(9, entities.get(i).getTeacher().getId());
    }

    @Override
    protected RowMapper<Lesson> rowMapper() {
        return lessonMapper;
    }

    @Override
    protected void update(PreparedStatement preparedStatement, Lesson entity) throws SQLException {
        preparedStatement.setString(1, entity.getDiscipline().getId());
        preparedStatement.setString(2, entity.getAudience().getId());
        preparedStatement.setString(3, entity.getLessonType().toString());
        preparedStatement.setObject(4, entity.getDate());
        preparedStatement.setObject(5, entity.getTimeStart());
        preparedStatement.setObject(6, entity.getTimeEnd());
        preparedStatement.setString(7, entity.getGroup().getId());
        preparedStatement.setString(8, entity.getTeacher().getId());
        preparedStatement.setString(9, entity.getId());
    }

    @Override
    public List<Lesson> findByDisciplineName(String disciplineName) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_DISCIPLINE_NAME_QUERY,
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, disciplineName);
            return preparedStatement;
        }, rowMapper());
    }
}
