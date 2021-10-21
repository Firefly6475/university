package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.service.DepartmentService;

import java.util.List;

@Controller
@RequestMapping(value = "/department")
public class DepartmentController extends GenericController<Department> {

    @Autowired
    DepartmentService departmentService;

    @GetMapping(value = "/all")
    public String showAllDepartments(@RequestParam(name = "page", defaultValue = "1") String pageNumber, Model model) {
        return showEntitiesWithList(pageNumber, departmentService.showAllDepartments(), model);
    }

    @Override
    protected String payload(List<Department> entities, Model model) {
        model.addAttribute("departments", entities);
        return "department/departmentsList";
    }
}
