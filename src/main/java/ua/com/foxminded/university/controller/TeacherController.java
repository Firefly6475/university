package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.controller.exception.WrongPageNumberException;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

import java.util.List;

@Controller
public class TeacherController {
    private static final Integer AMOUNT_PER_PAGE = 10;

    @Autowired
    TeacherService teacherService;

    @RequestMapping(value = "/teachers/list")
    public String showAllStudents(@RequestParam(name = "page", defaultValue = "1") Integer pageNumber, Model model) {
        Integer totalPages = getTotalPages(teacherService.showAllTeachers());
        model.addAttribute("totalPages", totalPages);
        if (pageNumber > totalPages || pageNumber <= 0) {
            throw new WrongPageNumberException();
        }
        model.addAttribute("teachers", teacherService.showAllTeachers(pageNumber));
        return "teacher/teachersList";
    }

    private Integer getTotalPages(List<Teacher> teachers) {
        return (teachers.size() / AMOUNT_PER_PAGE) + 1;
    }

}
