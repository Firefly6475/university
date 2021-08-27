package ua.com.foxminded.university.spring.dao.mapper;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.spring.dao.mapper.generic.AbstractCollectingRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class DepartmentMapper extends AbstractCollectingRowMapper<Department> {

    private final TeacherMapper teacherMapper;

    public DepartmentMapper(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    @Override
    protected Department mapRow(ResultSet rs, Department partialResult, int rowNum) throws SQLException {
        if (partialResult == null) {
            partialResult = Department.builder()
                    .id(rs.getString("department_id"))
                    .name(rs.getString("department_name"))
                    .teachers(new ArrayList<>())
                    .build();
        }
        partialResult.getTeachers().add(teacherMapper.mapRow(rs, null, rowNum));

        return partialResult;
    }

    @Override
    protected boolean isRelated(ResultSet rs, Department partialResult) throws SQLException {
        return partialResult.getId().equals((rs.getString("department_id")));
    }
}
