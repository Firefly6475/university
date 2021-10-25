package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.service.FacultyService;

import java.util.List;

@Controller
@RequestMapping(value = "/faculty")
public class FacultyController extends GenericController<Faculty> {

    @Autowired
    FacultyService facultyService;

    @GetMapping(value = "/all")
    public String showAllFaculties(@RequestParam(name = "page", defaultValue = "1") String pageNumber, Model model) {
        return showEntitiesWithList(pageNumber, facultyService.showAllFaculties(), model);
    }

    @GetMapping(value = "/{id}")
    public String showFacultyDetails(@PathVariable(name = "id") String id, Model model) {
        Faculty faculty = facultyService.findFacultyById(id);
        model.addAttribute("faculty", faculty);
        return "faculty/facultyDetails";
    }

    @Override
    protected String payload(List<Faculty> entities, Model model) {
        model.addAttribute("faculties", entities);
        return "faculty/facultiesList";
    }
}
