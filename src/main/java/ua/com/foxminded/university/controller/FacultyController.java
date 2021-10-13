package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.controller.exception.WrongPageNumberException;
import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.service.FacultyService;

import java.util.List;

@Controller
public class FacultyController {
    private static final Integer AMOUNT_PER_PAGE = 10;

    @Autowired
    FacultyService facultyService;

    @RequestMapping(value = "/faculties/list")
    public String showAllFaculties(@RequestParam(name = "page", defaultValue = "1") Integer pageNumber, Model model) {
        List<Faculty> faculties = facultyService.showAllFaculties();
        Integer totalPages = getTotalPages(faculties);
        model.addAttribute("totalPages", totalPages);
        if (pageNumber > totalPages || pageNumber <= 0) {
            throw new WrongPageNumberException();
        }
        if (faculties.size() > AMOUNT_PER_PAGE) {
            int startIndex = (pageNumber - 1) * AMOUNT_PER_PAGE;
            int endIndex = startIndex + AMOUNT_PER_PAGE;
            if (endIndex >= faculties.size()) {
                endIndex = faculties.size();
            }
            faculties = faculties.subList(startIndex, endIndex);
        }
        model.addAttribute("faculties", faculties);
        return "faculty/facultiesList";
    }
    
    private Integer getTotalPages(List<Faculty> faculties) {
        return (faculties.size() / AMOUNT_PER_PAGE) + 1;
    }
}
