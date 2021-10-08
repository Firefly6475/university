package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.spring.dao.Page;

@Controller
public class GroupController {

    @Autowired
    GroupService groupService;

    @RequestMapping(value = "/groups/list")
    public String showAllGroups(Model model) {
        model.addAttribute("groups", groupService.showAllGroups(new Page(1, 100)));
        return "group/groupsList";
    }
}
