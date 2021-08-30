package ua.com.foxminded.university.spring.dao.mapper;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.spring.dao.mapper.generic.AbstractCollectingRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class DisciplineMapper extends AbstractCollectingRowMapper<Discipline> {

    private final TeacherMapper teacherMapper;

    public DisciplineMapper(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    @Override
    protected Discipline mapRow(ResultSet rs, Discipline partialResult, int rowNum) throws SQLException {
        if (partialResult == null) {
            partialResult = Discipline.builder()
                    .withId(rs.getString("discipline_id"))
                    .withName(rs.getString("discipline_name"))
                    .withTeachers(new ArrayList<>())
                    .build();
        }
        partialResult.addTeacher(teacherMapper.mapRow(rs, null, rowNum));

        return partialResult;
    }

    @Override
    protected boolean isRelated(ResultSet rs, Discipline partialResult) throws SQLException {
        return partialResult.getId().equals((rs.getString("discipline_id")));
    }
}
