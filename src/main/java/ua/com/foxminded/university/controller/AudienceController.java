package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.service.AudienceService;
import ua.com.foxminded.university.spring.dao.Page;

@Controller
public class AudienceController {

    @Autowired
    AudienceService audienceService;

    @RequestMapping(value = "/audiences/list")
    public String showAllAudiences(Model model) {
        model.addAttribute("audiences", audienceService.showAllAudiences(new Page(1, 100)));
        return "/audience/audiencesList";
    }
}
