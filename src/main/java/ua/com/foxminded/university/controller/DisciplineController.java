package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.controller.exception.WrongPageNumberException;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.service.DisciplineService;

import java.util.List;

@Controller
public class DisciplineController {
    private static final Integer AMOUNT_PER_PAGE = 10;

    @Autowired
    DisciplineService disciplineService;

    @RequestMapping(value = "/disciplines/list")
    public String showAllDisciplines(@RequestParam(name = "page", defaultValue = "1") Integer pageNumber, Model model) {
        List<Discipline> disciplines = disciplineService.showAllDisciplines();
        Integer totalPages = getTotalPages(disciplines);
        model.addAttribute("totalPages", totalPages);
        if (pageNumber > totalPages || pageNumber <= 0) {
            throw new WrongPageNumberException();
        }
        if (disciplines.size() > AMOUNT_PER_PAGE) {
            int startIndex = (pageNumber - 1) * AMOUNT_PER_PAGE;
            int endIndex = startIndex + AMOUNT_PER_PAGE;
            if (endIndex >= disciplines.size()) {
                endIndex = disciplines.size();
            }
            disciplines = disciplines.subList(startIndex, endIndex);
        }
        model.addAttribute("disciplines", disciplines);        
        return "discipline/disciplinesList";
    }
    
    private Integer getTotalPages(List<Discipline> disciplines) {
        return (disciplines.size() / AMOUNT_PER_PAGE) + 1;
    }
}
