package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.controller.exception.WrongPageNumberException;
import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.service.DepartmentService;

import java.util.List;

@Controller
public class DepartmentController {
    private static final Integer AMOUNT_PER_PAGE = 10;

    @Autowired
    DepartmentService departmentService;

    @RequestMapping(value = "/departments/list")
    public String showAllDepartments(@RequestParam(name = "page", defaultValue = "1") Integer pageNumber, Model model) {
        List<Department> departments = departmentService.showAllDepartments();
        Integer totalPages = getTotalPages(departments);
        model.addAttribute("totalPages", totalPages);
        if (pageNumber > totalPages || pageNumber <= 0) {
            throw new WrongPageNumberException();
        }
        if (departments.size() > AMOUNT_PER_PAGE) {
            int startIndex = (pageNumber - 1) * AMOUNT_PER_PAGE;
            int endIndex = startIndex + AMOUNT_PER_PAGE;
            if (endIndex >= departments.size()) {
                endIndex = departments.size();
            }
            departments = departments.subList(startIndex, endIndex);
        }
        model.addAttribute("departments", departments);
        return "department/departmentsList";
    }

    private Integer getTotalPages(List<Department> departments) {
        return (departments.size() / AMOUNT_PER_PAGE) + 1;
    }
}
