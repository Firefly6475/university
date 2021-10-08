package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.service.FacultyService;
import ua.com.foxminded.university.spring.dao.Page;

@Controller
public class FacultyController {

    @Autowired
    FacultyService facultyService;

    @RequestMapping(value = "/faculties/list")
    public String showAllFaculties(Model model) {
        model.addAttribute("faculties", facultyService.showAllFaculties(new Page(1, 100)));
        return "faculty/facultiesList";
    }
}
