package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.controller.exception.WrongPageNumberException;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.StudentService;

import java.util.List;

@Controller
public class StudentController {
    private static final Integer AMOUNT_PER_PAGE = 10;

    @Autowired
    StudentService studentService;

    @RequestMapping(value = "/students/list")
    public String showAllStudents(@RequestParam(name = "page", defaultValue = "1") Integer pageNumber, Model model) {
        Integer totalPages = getTotalPages(studentService.showAllStudents());
        model.addAttribute("totalPages", totalPages);
        if (pageNumber > totalPages || pageNumber <= 0) {
            throw new WrongPageNumberException();
        }
        model.addAttribute("students", studentService.showAllStudents(pageNumber));
        return "student/studentsList";
    }

    private Integer getTotalPages(List<Student> students) {
        return (students.size() / AMOUNT_PER_PAGE) + 1;
    }
}
