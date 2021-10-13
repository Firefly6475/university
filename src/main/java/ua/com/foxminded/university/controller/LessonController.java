package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.controller.exception.WrongPageNumberException;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.service.LessonService;

import java.util.List;

@Controller
public class LessonController {
    private static final Integer AMOUNT_PER_PAGE = 10;

    @Autowired
    LessonService lessonService;

    @RequestMapping(value = "/lessons/list")
    public String showAllLessons(@RequestParam(name = "page", defaultValue = "1") Integer pageNumber, Model model) {
        List<Lesson> lessons = lessonService.showAllLessons();
        Integer totalPages = getTotalPages(lessons);
        model.addAttribute("totalPages", totalPages);
        if (pageNumber > totalPages || pageNumber <= 0) {
            throw new WrongPageNumberException();
        }
        if (lessons.size() > AMOUNT_PER_PAGE) {
            int startIndex = (pageNumber - 1) * AMOUNT_PER_PAGE;
            int endIndex = startIndex + AMOUNT_PER_PAGE;
            if (endIndex >= lessons.size()) {
                endIndex = lessons.size();
            }
            lessons = lessons.subList(startIndex, endIndex);
        }
        model.addAttribute("lessons", lessons);
        return "lesson/lessonsList";
    }

    private Integer getTotalPages(List<Lesson> lessons) {
        return (lessons.size() / AMOUNT_PER_PAGE) + 1;
    }
}
