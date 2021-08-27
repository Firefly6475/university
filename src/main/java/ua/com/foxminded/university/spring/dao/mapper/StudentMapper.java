package ua.com.foxminded.university.spring.dao.mapper;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.spring.dao.mapper.generic.AbstractCollectingRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentMapper extends AbstractCollectingRowMapper<Student> {
    @Override
    protected Student mapRow(ResultSet rs, Student partialResult, int rowNum) throws SQLException {
        if (partialResult == null) {
            partialResult = Student.builder()
                    .id(rs.getString("student_id"))
                    .name(rs.getString("student_name"))
                    .birthday(rs.getDate("student_birthday").toLocalDate())
                    .course(rs.getInt("student_course"))
                    .build();
        }

        return partialResult;
    }

    @Override
    protected boolean isRelated(ResultSet rs, Student partialResult) throws SQLException {
        return partialResult.getId().equals((rs.getString("student_id")));
    }
}
