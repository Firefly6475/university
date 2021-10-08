package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.spring.dao.Page;

@Controller
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @RequestMapping(value = "/teachers/list")
    public String showAllStudents(Model model) {
        model.addAttribute("teachers", teacherService.showAllTeachers(new Page(1, 100)));
        return "teacher/teachersList";
    }
}
