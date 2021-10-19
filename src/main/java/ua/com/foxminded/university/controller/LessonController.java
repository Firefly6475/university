package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.service.LessonService;

import java.util.List;

@Controller
public class LessonController extends GenericController<Lesson> {

    @Autowired
    LessonService lessonService;

    @RequestMapping(value = "/lessons/list")
    public String showAllLessons(@RequestParam(name = "page", defaultValue = "1") String pageNumber, Model model) {
        return showEntitiesWithList(pageNumber, lessonService.showAllLessons(), model);

    }

    @Override
    protected String payload(List<Lesson> entities, Model model) {
        model.addAttribute("lessons", entities);
        return "lesson/lessonsList";
    }
}
