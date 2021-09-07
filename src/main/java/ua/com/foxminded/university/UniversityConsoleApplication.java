package ua.com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.impl.TeacherServiceImpl;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.config.JdbcConfig;
import ua.com.foxminded.university.spring.dao.TeacherDao;

public class UniversityConsoleApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JdbcConfig.class);
        TeacherDao teacherDao = context.getBean(TeacherDao.class);
        Validator validator = context.getBean(Validator.class);
        TeacherService teacherService = new TeacherServiceImpl(teacherDao, validator);
        Teacher teacher = Teacher.builder()
                .withId(null)
                .withName("ds")
                .withBirthday(null)
                .build();
        teacherService.registerTeacher(teacher);
    }
}
