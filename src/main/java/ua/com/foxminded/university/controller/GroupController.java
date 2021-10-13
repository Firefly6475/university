package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.controller.exception.WrongPageNumberException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import java.util.List;

@Controller
public class GroupController {
    private static final Integer AMOUNT_PER_PAGE = 10;

    @Autowired
    GroupService groupService;

    @RequestMapping(value = "/groups/list")
    public String showAllGroups(@RequestParam(name = "page", defaultValue = "1") Integer pageNumber, Model model) {
        List<Group> groups = groupService.showAllGroups();
        Integer totalPages = getTotalPages(groups);
        model.addAttribute("totalPages", totalPages);
        if (pageNumber > totalPages || pageNumber <= 0) {
            throw new WrongPageNumberException();
        }
        if (groups.size() > AMOUNT_PER_PAGE) {
            int startIndex = (pageNumber - 1) * AMOUNT_PER_PAGE;
            int endIndex = startIndex + AMOUNT_PER_PAGE;
            if (endIndex >= groups.size()) {
                endIndex = groups.size();
            }
            groups = groups.subList(startIndex, endIndex);
        }
        model.addAttribute("groups", groups);
        return "group/groupsList";
    }

    private Integer getTotalPages(List<Group> groups) {
        return (groups.size() / AMOUNT_PER_PAGE) + 1;
    }
}
