package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.model.Department;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.DepartmentService;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityIsNotEmptyException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.DepartmentDao;
import ua.com.foxminded.university.spring.dao.TeacherDao;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentDao departmentDao;
    private final TeacherDao teacherDao;
    private final Validator<Department> validator;

    @Override
    public void addDepartment(Department department) {
        log.info("Adding department started");
        log.info("Checking department name existence in database");
        if (departmentDao.findByName(department.getName()).isPresent()) {
            log.error("Department already exists in database");
            throw new EntityAlreadyExistException("Entity with specified name is already exists");
        }
        log.info("Validating department...");
        validator.validate(department);
        departmentDao.save(department);
        log.info("Department successfully added");
    }

    @Override
    public Department findDepartmentById(String id) {
        log.info("Finding department by id in database started");
        return departmentDao.findById(id).orElseThrow(() -> {
            log.error("Department by id not found");
            return new EntityNotFoundException("No specified entity found");
        });
    }

    @Override
    public Department findDepartmentByName(String name) {
        log.info("Finding department by name in database started");
        return departmentDao.findByName(name).orElseThrow(() -> {
            log.error("Department by name not found");
            return new EntityNotFoundException("No specified entity found");
        });

    }

    @Override
    public List<Department> showAllDepartments() {
        log.info("Getting all departments started");
        return departmentDao.findAll();
    }

    @Override
    public void deleteDepartment(String id) {
        log.info("Department deletion by id started");
        log.info("Checking department existence");
        Optional<Department> departmentInDb = departmentDao.findById(id);
        if (!departmentInDb.isPresent()) {
            log.error("No department found for deletion");
            throw new EntityNotFoundException("No specified entity found");
        }
        log.info("Checking teachers existence in department");
        if (!departmentInDb.get().getTeachers().isEmpty()) {
            log.error("Department still has teachers");
            throw new EntityIsNotEmptyException("Can't delete non-empty entity");
        }
        departmentDao.deleteById(id);
        log.info("Successful department deletion");
    }

    @Override
    public void editDepartment(Department department) {
        log.info("Department editing started");
        log.info("Checking department existence");
        Optional<Department> departmentInDb = departmentDao.findById(department.getId());
        if (!departmentInDb.isPresent()) {
            log.error("Department to edit not found");
            throw new EntityNotFoundException("No specified entity found");
        }
        log.info("Checking if the same name already exists");
        if (departmentDao.findByName(department.getName()).isPresent()) {
            log.error("Name already exists");
            throw new EntityAlreadyExistException("Specified name already exists");
        }
        log.info("Validating department...");
        validator.validate(department);
        departmentDao.update(department);
        log.info("Department successfully edited");
    }

    @Override
    public void addTeacherToDepartment(String departmentId, String teacherId) {
        log.info("Adding teacher to department started");
        log.info("Checking department existence");
        Optional<Department> departmentInDb = departmentDao.findById(departmentId);
        if (!departmentInDb.isPresent()) {
            log.error("Department to add teacher in not found");
            throw new EntityNotFoundException("Specified department not found");
        }
        log.info("Checking teacher existence");
        Optional<Teacher> teacherInDb = teacherDao.findById(teacherId);
        if (!teacherInDb.isPresent()) {
            log.error("Teacher to add not found");
            throw new EntityNotFoundException("Specified teacher not found");
        }
        log.info("Checking if teacher is already in a department");
        if (departmentInDb.get().getTeachers().contains(teacherInDb.get())) {
            log.error("Teacher is already in the department");
            throw new EntityAlreadyExistException("Specified teacher already in department");
        }
        departmentDao.addTeacherToDepartment(departmentId, teacherId);
        log.info("Successfully added teacher to department");
    }

    @Override
    public void removeTeacherFromDepartment(String departmentId, String teacherId) {
        log.info("Teacher from department removal started");
        log.info("Checking department existence");
        Optional<Department> departmentInDb = departmentDao.findById(departmentId);
        if (!departmentInDb.isPresent()) {
            log.error("Department to remove teacher from not found");
            throw new EntityNotFoundException("Specified department not found");
        }
        log.info("Checking teacher existence");
        Optional<Teacher> teacherInDb = teacherDao.findById(teacherId);
        if (!teacherInDb.isPresent()) {
            log.error("Teacher to remove from department not found");
            throw new EntityNotFoundException("Specified teacher not found");
        }
        log.info("Checking if teacher is in a department");
        if (!departmentInDb.get().getTeachers().contains(teacherInDb.get())) {
            log.error("No teacher in specified department ");
            throw new EntityNotFoundException("Specified teacher is not in a department");
        }
        departmentDao.removeTeacherFromDepartment(departmentId, teacherId);
        log.info("Successfully removed teacher from department");
    }
}
