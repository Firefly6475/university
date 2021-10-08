package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.service.DepartmentService;
import ua.com.foxminded.university.spring.dao.Page;

@Controller
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @RequestMapping(value = "/departments/list")
    public String showAllDepartments(Model model) {
        model.addAttribute("departments", departmentService.showAllDepartments(new Page(1, 100)));
        return "department/departmentsList";
    }
}
