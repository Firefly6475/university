package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.service.DisciplineService;
import ua.com.foxminded.university.spring.dao.Page;

@Controller
public class DisciplineController {

    @Autowired
    DisciplineService disciplineService;

    @RequestMapping(value = "/disciplines/list")
    public String showAllDisciplines(Model model) {
        model.addAttribute("disciplines", disciplineService.showAllDisciplines(new Page(1, 100)));
        return "discipline/disciplinesList";
    }
}
