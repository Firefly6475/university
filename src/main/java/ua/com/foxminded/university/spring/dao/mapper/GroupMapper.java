package ua.com.foxminded.university.spring.dao.mapper;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.spring.dao.mapper.generic.AbstractCollectingRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class GroupMapper extends AbstractCollectingRowMapper<Group> {

    private final StudentMapper studentMapper;

    public GroupMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    protected Group mapRow(ResultSet rs, Group partialResult, int rowNum) throws SQLException {
        if (partialResult == null) {
            partialResult = Group.builder()
                    .id(rs.getString("group_id"))
                    .name(rs.getString("group_name"))
                    .students(new ArrayList<>())
                    .build();
        }
        partialResult.getStudents().add(studentMapper.mapRow(rs, null, rowNum));

        return partialResult;
    }

    @Override
    protected boolean isRelated(ResultSet rs, Group partialResult) throws SQLException {
        return partialResult.getId().equals((rs.getString("group_id")));
    }
}
