package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import java.util.List;

@Controller
@RequestMapping(value = "/group")
public class GroupController extends GenericController<Group> {

    @Autowired
    GroupService groupService;

    @GetMapping(value = "/all")
    public String showAllGroups(@RequestParam(name = "page", defaultValue = "1", value = "page") String pageNumber, Model model) {
        return showEntitiesWithList(pageNumber, groupService.showAllGroups(), model);
    }

    @GetMapping(value = "/{id}")
    public String showGroupDetails(@PathVariable(name = "id") String id, Model model) {
        Group group = groupService.findGroupById(id);
        model.addAttribute("group", group);
        return "group/groupDetails";
    }

    @Override
    protected String payload(List<Group> entities, Model model) {
        model.addAttribute("groups", entities);
        return "group/groupsList";
    }
}
