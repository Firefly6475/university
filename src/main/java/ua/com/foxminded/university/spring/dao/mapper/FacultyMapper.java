package ua.com.foxminded.university.spring.dao.mapper;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.spring.dao.mapper.generic.AbstractCollectingRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class FacultyMapper extends AbstractCollectingRowMapper<Faculty> {

    private final GroupMapper groupMapper;
    private final StudentMapper studentMapper;

    public FacultyMapper(GroupMapper groupMapper, StudentMapper studentMapper) {
        this.groupMapper = groupMapper;
        this.studentMapper = studentMapper;
    }

    @Override
    protected Faculty mapRow(ResultSet rs, Faculty partialResult, int rowNum) throws SQLException {
        if (partialResult == null) {
            partialResult = Faculty.builder()
                    .withId(rs.getString("faculty_id"))
                    .withName(rs.getString("faculty_name"))
                    .withGroups(new ArrayList<>())
                    .build();
            if (rs.getString("group_id") != null) {
                partialResult.addGroup(groupMapper.mapRow(rs, null, rowNum));
            }
            return partialResult;
        }
        int lastListItemIndex = partialResult.getGroups().size() - 1;
        Group lastGroup = partialResult.getGroups().get(lastListItemIndex);
        if (groupMapper.isRelated(rs, partialResult.getGroups().get(lastListItemIndex))) {
            lastGroup.addStudent(studentMapper.mapRow(rs, null, rowNum));
        } else {
            partialResult.getGroups().add(groupMapper.mapRow(rs, null, rowNum));
        }

        return partialResult;
    }

    @Override
    protected boolean isRelated(ResultSet rs, Faculty partialResult) throws SQLException {
        return partialResult.getId().equals((rs.getString("faculty_id")));
    }
}
