package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.spring.dao.Page;

import java.util.List;

public interface DepartmentService {
    void addDepartment(Department department);

    Department findDepartmentById(String id);

    Department findDepartmentByName(String name);

    List<Department> showAllDepartments(Page page);

    void deleteDepartment(String id);

    void editDepartment(Department department);

    void addTeacherToDepartment(String departmentId, String teacherId);

    void removeTeacherFromDepartment(String departmentId, String teacherId);
}
