package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.Page;
import ua.com.foxminded.university.spring.dao.StudentDao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentDao studentDao;
    private final Validator<Student> validator;

    @Override
    public void registerStudent(Student student) {
        if (studentDao.findByEmail(student.getEmail()).isPresent()) {
            throw new EntityAlreadyExistException("Entity with specified email is already exists");
        }
        validator.validate(student);
        studentDao.save(student);
    }

    @Override
    public List<Student> showAllStudents(Page page) {
        return studentDao.findAll(page);
    }

    @Override
    public Student findStudentById(String id) {
        return studentDao.findById(id).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));
    }

    @Override
    public Student findStudentByEmail(String email) {
        return studentDao.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));
    }

    @Override
    public void deleteStudent(String id) {
        if (!studentDao.findById(id).isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        studentDao.deleteById(id);
    }

    @Override
    public void editStudent(Student student) {
        Optional<Student> studentDb = studentDao.findById(student.getId());
        if (!studentDb.isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        Student studentToEdit = studentDb.get();
        if (isEmailChanged(student, studentToEdit)) {
            if (studentDao.findByEmail(student.getEmail()).isPresent()) {
                throw new EntityAlreadyExistException("Specified email already exists");
            }
        }
        validator.validate(student);
        studentDao.update(student);
    }

    protected boolean isEmailChanged(Student student, Student studentToEdit) {
        return !Objects.equals(studentToEdit.getEmail(), student.getEmail());
    }
}
