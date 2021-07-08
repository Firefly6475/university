package ua.com.foxminded.university.spring.dao;

import ua.com.foxminded.university.model.Department;

public interface DepartmentDao extends CrudDao<Department> {
    void addTeacherToDepartment(String departmentId, String teacherId);
    
    void addAllTeachersToDepartment(Department department);
    
    void removeTeacherFromDepartment(String departmentId, String teacherId);
}
