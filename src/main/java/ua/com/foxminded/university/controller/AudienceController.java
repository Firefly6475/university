package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.controller.exception.WrongPageNumberException;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.service.AudienceService;

import java.util.List;

@Controller
public class AudienceController {
    private static final Integer AMOUNT_PER_PAGE = 10;

    @Autowired
    AudienceService audienceService;

    @RequestMapping(value = "/audiences/list")
    public String showAllAudiences(@RequestParam(name = "page", defaultValue = "1") Integer pageNumber, Model model) {
        Integer totalPages = getTotalPages(audienceService.showAllAudiences());
        model.addAttribute("totalPages", totalPages);
        if (pageNumber > totalPages || pageNumber <= 0) {
            throw new WrongPageNumberException();
        }
        model.addAttribute("audiences", audienceService.showAllAudiences(1));
        return "audience/audiencesList";
    }

    private Integer getTotalPages(List<Audience> audiences) {
        return (audiences.size() / AMOUNT_PER_PAGE) + 1;
    }
}
