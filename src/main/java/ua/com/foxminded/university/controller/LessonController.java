package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.spring.dao.Page;

@Controller
public class LessonController {

    @Autowired
    LessonService lessonService;

    @RequestMapping(value = "/lessons/list")
    public String showAllLessons(Model model) {
        model.addAttribute("lessons",lessonService.showAllLessons(new Page(1, 100)));
        return "lesson/lessonsList";
    }
}
