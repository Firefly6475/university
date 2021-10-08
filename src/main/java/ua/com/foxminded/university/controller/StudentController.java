package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.spring.dao.Page;

@Controller
public class StudentController {

    @Autowired
    StudentService studentService;

    @RequestMapping(value = "/students/list")
    public String showAllStudents(Model model) {
        model.addAttribute("students", studentService.showAllStudents(new Page(1, 100)));
        return "student/studentsList";
    }
}
