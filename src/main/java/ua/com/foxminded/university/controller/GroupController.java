package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import java.util.List;

@Controller
public class GroupController extends GenericController<Group> {

    @Autowired
    GroupService groupService;

    @RequestMapping(value = "/groups/list")
    public String showAllGroups(@RequestParam(name = "page", defaultValue = "1", value = "page") String pageNumber, Model model) {
        return showEntitiesWithList(pageNumber, groupService.showAllGroups(), model);
    }

    @Override
    protected String payload(List<Group> entities, Model model) {
        model.addAttribute("groups", entities);
        return "group/groupsList";
    }
}
