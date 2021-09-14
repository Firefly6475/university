package ua.com.foxminded.university.spring.dao.mapper;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.spring.dao.mapper.generic.AbstractCollectingRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TeacherMapper extends AbstractCollectingRowMapper<Teacher> {
    @Override
    protected Teacher mapRow(ResultSet rs, Teacher partialResult, int rowNum) throws SQLException {
        return Teacher.builder()
                .withId(rs.getString("teacher_id"))
                .withEmail(rs.getString("teacher_email"))
                .withPassword(rs.getString("teacher_password"))
                .withName(rs.getString("teacher_name"))
                .withBirthday(rs.getDate("teacher_birthday").toLocalDate())
                .build();
    }

    @Override
    protected boolean isRelated(ResultSet rs, Teacher partialResult) throws SQLException {
        return partialResult.getId().equals((rs.getString("teacher_id")));
    }

    public Teacher mapTeacherLessonRow(ResultSet rs) throws SQLException {
        return Teacher.builder()
                .withId(rs.getString("lesson_teacher_id"))
                .withEmail(rs.getString("lesson_teacher_email"))
                .withPassword(rs.getString("lesson_teacher_password"))
                .withName(rs.getString("lesson_teacher_name"))
                .withBirthday(rs.getDate("lesson_teacher_birthday").toLocalDate())
                .build();
    }
}
