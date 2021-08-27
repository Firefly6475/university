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
        if (partialResult == null) {
            partialResult = Teacher.builder()
                    .id(rs.getString("teacher_id"))
                    .name(rs.getString("teacher_name"))
                    .birthday(rs.getDate("teacher_birthday").toLocalDate())
                    .salary(rs.getInt("teacher_salary"))
                    .build();
        }

        return partialResult;
    }

    @Override
    protected boolean isRelated(ResultSet rs, Teacher partialResult) throws SQLException {
        return partialResult.getId().equals((rs.getString("teacher_id")));
    }

    public Teacher mapTeacherLessonRow(ResultSet rs, int rowNum) throws SQLException {
        return Teacher.builder()
                .id(rs.getString("lesson_teacher_id"))
                .name(rs.getString("lesson_teacher_name"))
                .birthday(rs.getDate("lesson_teacher_birthday").toLocalDate())
                .salary(rs.getInt("lesson_teacher_salary"))
                .build();
    }
}
