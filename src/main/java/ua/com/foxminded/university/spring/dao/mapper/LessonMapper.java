package ua.com.foxminded.university.spring.dao.mapper;

import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.LessonType;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.spring.dao.mapper.generic.AbstractCollectingRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class LessonMapper extends AbstractCollectingRowMapper<Lesson> {

    private final DisciplineMapper disciplineMapper;
    private final AudienceMapper audienceMapper;
    private final GroupMapper groupMapper;
    private final TeacherMapper teacherMapper;
    private final StudentMapper studentMapper;

    public LessonMapper(DisciplineMapper disciplineMapper, AudienceMapper audienceMapper, GroupMapper groupMapper,
                        TeacherMapper teacherMapper, StudentMapper studentMapper) {
        this.disciplineMapper = disciplineMapper;
        this.audienceMapper = audienceMapper;
        this.groupMapper = groupMapper;
        this.teacherMapper = teacherMapper;
        this.studentMapper = studentMapper;
    }

    @Override
    protected Lesson mapRow(ResultSet rs, Lesson partialResult, int rowNum) throws SQLException {
        if (partialResult == null) {
            Discipline discipline = disciplineMapper.mapRow(rs, null, rowNum);
            Audience audience = audienceMapper.mapRow(rs, null, rowNum);
            Group group = groupMapper.mapRow(rs, null, rowNum);
            Teacher lessonTeacher = teacherMapper.mapTeacherLessonRow(rs, rowNum);
            partialResult = Lesson.builder()
                    .id(rs.getString("lesson_id"))
                    .discipline(discipline)
                    .audience(audience)
                    .lessonType(LessonType.valueOf(rs.getString("lesson_type")))
                    .group(group)
                    .teacher(lessonTeacher)
                    .date(rs.getDate("date").toLocalDate())
                    .timeStart(rs.getTime("time_start").toLocalTime())
                    .timeEnd(rs.getTime("time_end").toLocalTime())
                    .build();
        }
        populateDisciplineGroupLists(partialResult.getDiscipline(), partialResult.getGroup(), rs, rowNum);

        return partialResult;
    }

    @Override
    protected boolean isRelated(ResultSet rs, Lesson partialResult) throws SQLException {
        return partialResult.getId().equals((rs.getString("lesson_id")));
    }

    private void populateDisciplineGroupLists(Discipline discipline, Group group, ResultSet rs, int rowNum) throws SQLException {
        Teacher teacher = teacherMapper.mapRow(rs, null, rowNum);
        Student student = studentMapper.mapRow(rs, null, rowNum);
        if (!discipline.getTeachers().contains(teacher)) {
            discipline.addTeacher(teacher);
        }
        if (!group.getStudents().contains(student)) {
            group.addStudent(student);
        }
    }
}
