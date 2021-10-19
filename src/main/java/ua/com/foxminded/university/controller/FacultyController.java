package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.service.FacultyService;

import java.util.List;

@Controller
public class FacultyController extends GenericController<Faculty> {

    @Autowired
    FacultyService facultyService;

    @RequestMapping(value = "/faculties/list")
    public String showAllFaculties(@RequestParam(name = "page", defaultValue = "1") String pageNumber, Model model) {
        return showEntitiesWithList(pageNumber, facultyService.showAllFaculties(), model);
    }

    @Override
    protected String payload(List<Faculty> entities, Model model) {
        model.addAttribute("faculties", entities);
        return "faculty/facultiesList";
    }
}
