package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

import java.util.List;

@Controller
public class TeacherController extends GenericController<Teacher> {

    @Autowired
    TeacherService teacherService;

    @RequestMapping(value = "/teachers/list")
    public String showAllStudents(@RequestParam(name = "page", defaultValue = "1") String stringPageNumber, Model model) {
        List<Teacher> allTeachers = teacherService.showAllTeachers();
        Integer pageNumber = parsePageNumber(stringPageNumber);
        Integer totalPages = getTotalPages(allTeachers);
        pageNumber = validatePageNumber(pageNumber, totalPages);
        List<Teacher> pagedTeachers = teacherService.showAllTeachers(pageNumber);
        return showGenericEntities(totalPages, pagedTeachers, model);
    }

    @Override
    protected String payload(List<Teacher> entities, Model model) {
        model.addAttribute("teachers", entities);
        return "teacher/teachersList";
    }
}
