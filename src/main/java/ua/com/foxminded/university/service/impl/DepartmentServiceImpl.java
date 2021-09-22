package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.DepartmentService;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityIsNotEmptyException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.DepartmentDao;
import ua.com.foxminded.university.spring.dao.Page;
import ua.com.foxminded.university.spring.dao.TeacherDao;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentDao departmentDao;
    private final TeacherDao teacherDao;
    private final Validator<Department> validator;

    @Override
    public void addDepartment(Department department) {
        if (departmentDao.findByName(department.getName()).isPresent()) {
            throw new EntityAlreadyExistException("Entity with specified name is already exists");
        }
        validator.validate(department);
        departmentDao.save(department);
    }

    @Override
    public Department findDepartmentById(String id) {
        return departmentDao.findById(id).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));
    }

    @Override
    public Department findDepartmentByName(String name) {
        return departmentDao.findByName(name).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));

    }

    @Override
    public List<Department> showAllDepartments(Page page) {
        return departmentDao.findAll(page);
    }

    @Override
    public void deleteDepartment(String id) {
        Optional<Department> departmentInDb = departmentDao.findById(id);
        if (!departmentInDb.isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        if (!departmentInDb.get().getTeachers().isEmpty()) {
            throw new EntityIsNotEmptyException("Can't delete non-empty entity");
        }
        departmentDao.deleteById(id);
    }

    @Override
    public void editDepartment(Department department) {
        Optional<Department> departmentInDb = departmentDao.findById(department.getId());
        if (!departmentInDb.isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        if (departmentDao.findByName(department.getName()).isPresent()) {
            throw new EntityAlreadyExistException("Specified name already exists");
        }
        validator.validate(department);
        departmentDao.update(department);
    }

    @Override
    public void addTeacherToDepartment(String departmentId, String teacherId) {
        Optional<Department> departmentInDb = departmentDao.findById(departmentId);
        if (!departmentInDb.isPresent()) {
            throw new EntityNotFoundException("Specified department not found");
        }

        Optional<Teacher> teacherInDb = teacherDao.findById(teacherId);
        if (!teacherInDb.isPresent()) {
            throw new EntityNotFoundException("Specified teacher not found");
        }

        if (departmentInDb.get().getTeachers().contains(teacherInDb.get())) {
            throw new EntityAlreadyExistException("Specified teacher already in department");
        }
        departmentDao.addTeacherToDepartment(departmentId, teacherId);
    }

    @Override
    public void removeTeacherFromDepartment(String departmentId, String teacherId) {
        Optional<Department> departmentInDb = departmentDao.findById(departmentId);
        if (!departmentInDb.isPresent()) {
            throw new EntityNotFoundException("Specified department not found");
        }

        Optional<Teacher> teacherInDb = teacherDao.findById(teacherId);
        if (!teacherInDb.isPresent()) {
            throw new EntityNotFoundException("Specified teacher not found");
        }

        if (!departmentInDb.get().getTeachers().contains(teacherInDb.get())) {
            throw new EntityNotFoundException("Specified teacher is not in a department");
        }

        departmentDao.removeTeacherFromDepartment(departmentId, teacherId);
    }
}
