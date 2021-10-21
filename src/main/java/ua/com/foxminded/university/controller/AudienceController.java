package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.model.Audience;
import ua.com.foxminded.university.service.AudienceService;

import java.util.List;

@Controller
@RequestMapping(value = "/audience")
public class AudienceController extends GenericController<Audience> {

    @Autowired
    AudienceService audienceService;

    @GetMapping(value = "/all")
    public String showAllAudiences(@RequestParam(name = "page", defaultValue = "1") String stringPageNumber, Model model) {
        List<Audience> allAudiences = audienceService.showAllAudiences();
        Integer pageNumber = parsePageNumber(stringPageNumber);
        Integer totalPages = getTotalPages(allAudiences);
        pageNumber = validatePageNumber(pageNumber, totalPages);
        List<Audience> pagedAudiences = audienceService.showAllAudiences(pageNumber);
        return showGenericEntities(totalPages, pagedAudiences, model);
    }

    @Override
    protected String payload(List<Audience> entities, Model model) {
        model.addAttribute("audiences", entities);
        return "audience/audiencesList";
    }
}
