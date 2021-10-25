package ua.com.foxminded.university.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping(value = "{id}")
    public String showDepartmentDetails(@PathVariable(name = "id") String id, Model model) {
        Department department = departmentService.findDepartmentById(id);
        model.addAttribute("department", department);
        return "department/departmentDetails";
    }

    @Override
    protected String payload(List<Department> entities, Model model) {
        model.addAttribute("departments", entities);
        return "department/departmentsList";
    }
}
