package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.service.DisciplineService;

import java.util.List;

@Controller
public class DisciplineController extends GenericController<Discipline> {

    @Autowired
    DisciplineService disciplineService;

    @RequestMapping(value = "/disciplines/list")
    public String showAllDisciplines(@RequestParam(name = "page", defaultValue = "1") String pageNumber, Model model) {
        return showEntitiesWithList(pageNumber, disciplineService.showAllDisciplines(), model);
    }

    @Override
    protected String payload(List<Discipline> entities, Model model) {
        model.addAttribute("disciplines", entities);
        return "discipline/disciplinesList";
    }
}
