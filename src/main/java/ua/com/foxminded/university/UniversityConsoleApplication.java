package ua.com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.spring.config.JdbcConfig;
import ua.com.foxminded.university.spring.dao.AudienceDao;
import ua.com.foxminded.university.spring.dao.DepartmentDao;
import ua.com.foxminded.university.spring.dao.DisciplineDao;
import ua.com.foxminded.university.spring.dao.FacultyDao;
import ua.com.foxminded.university.spring.dao.GroupDao;
import ua.com.foxminded.university.spring.dao.LessonDao;
import ua.com.foxminded.university.spring.dao.StudentDao;
import ua.com.foxminded.university.spring.dao.TeacherDao;

import java.util.List;

public class UniversityConsoleApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JdbcConfig.class);

        StudentDao studentDao = context.getBean(StudentDao.class);
        TeacherDao teacherDao = context.getBean(TeacherDao.class);
        GroupDao groupDao = context.getBean(GroupDao.class);
        DepartmentDao departmentDao = context.getBean(DepartmentDao.class);
        DisciplineDao disciplineDao = context.getBean(DisciplineDao.class);
        FacultyDao facultyDao = context.getBean(FacultyDao.class);
        AudienceDao audienceDao = context.getBean(AudienceDao.class);
        LessonDao lessonDao = context.getBean(LessonDao.class);

        List<Lesson> lessons = lessonDao.findAll();
        System.out.println(lessons);
    }
}
