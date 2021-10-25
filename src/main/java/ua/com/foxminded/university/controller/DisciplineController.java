package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.service.DisciplineService;

import java.util.List;

@Controller
@RequestMapping(value = "/discipline")
public class DisciplineController extends GenericController<Discipline> {

    @Autowired
    DisciplineService disciplineService;

    @GetMapping(value = "/all")
    public String showAllDisciplines(@RequestParam(name = "page", defaultValue = "1") String pageNumber, Model model) {
        return showEntitiesWithList(pageNumber, disciplineService.showAllDisciplines(), model);
    }

    @GetMapping(value = "/{id}")
    public String showDisciplineDetails(@PathVariable(name = "id") String id, Model model) {
        Discipline discipline = disciplineService.findDisciplineById(id);
        model.addAttribute("discipline", discipline);
        return "discipline/disciplineDetails";
    }

    @Override
    protected String payload(List<Discipline> entities, Model model) {
        model.addAttribute("disciplines", entities);
        return "discipline/disciplinesList";
    }
}
