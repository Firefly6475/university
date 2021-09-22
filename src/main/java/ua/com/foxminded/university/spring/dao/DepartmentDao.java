package ua.com.foxminded.university.spring.dao;

import ua.com.foxminded.university.model.Department;

import java.util.Optional;

public interface DepartmentDao extends CrudDao<Department> {
    void addTeacherToDepartment(String departmentId, String teacherId);
    
    void addAllTeachersToDepartment(Department department);
    
    void removeTeacherFromDepartment(String departmentId, String teacherId);

    Optional<Department> findByName(String name);
}
