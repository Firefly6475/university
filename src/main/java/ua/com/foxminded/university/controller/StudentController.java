package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.StudentService;

import java.util.List;

@Controller
public class StudentController extends GenericController<Student> {

    @Autowired
    StudentService studentService;

    @RequestMapping(value = "/students/list")
    public String showAllStudents(@RequestParam(name = "page", defaultValue = "1") String stringPageNumber, Model model) {
        List<Student> allStudents = studentService.showAllStudents();
        Integer pageNumber = parsePageNumber(stringPageNumber);
        Integer totalPages = getTotalPages(allStudents);
        pageNumber = validatePageNumber(pageNumber, totalPages);
        List<Student> pagedStudents = studentService.showAllStudents(pageNumber);
        return showGenericEntities(totalPages, pagedStudents, model);
    }

    @Override
    protected String payload(List<Student> entities, Model model) {
        model.addAttribute("students", entities);
        return "student/studentsList";
    }
}
