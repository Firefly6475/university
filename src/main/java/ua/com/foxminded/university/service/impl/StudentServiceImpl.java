package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
@Slf4j
@Service
public class StudentServiceImpl implements StudentService {
    private static final Integer ENTITIES_ON_PAGE = 10;

    private final StudentDao studentDao;
    private final Validator<Student> validator;

    @Override
    public void registerStudent(Student student) {
        log.info("Student registration started");
        log.info("Checking student email existence in database");
        if (studentDao.findByEmail(student.getEmail()).isPresent()) {
            log.error("Student already exists in database");
            throw new EntityAlreadyExistException("Entity with specified email is already exists");
        }
        log.info("Validating student...");
        validator.validate(student);
        studentDao.save(student);
        log.info("Student successfully saved");
    }

    @Override
    public List<Student> showAllStudents() {
        log.info("Getting all students started");
        return studentDao.findAll();
    }

    @Override
    public List<Student> showAllStudents(Integer pageNumber) {
        log.info("Getting all students started");
        return studentDao.findAll(generatePage(pageNumber));
    }

    @Override
    public Student findStudentById(String id) {
        log.info("Finding student by id in database started");
        return studentDao.findById(id).orElseThrow(() -> {
            log.error("Student by id not found");
            return new EntityNotFoundException("No specified entity found");
        });
    }

    @Override
    public Student findStudentByEmail(String email) {
        log.info("Finding student by email in database started");
        return studentDao.findByEmail(email).orElseThrow(() -> {
            log.error("Student by email not found");
            return new EntityNotFoundException("No specified entity found");
        });
    }

    @Override
    public void deleteStudent(String id) {
        log.info("Student deletion by id started");
        log.info("Checking student existence");
        if (!studentDao.findById(id).isPresent()) {
            log.error("No student found for deletion");
            throw new EntityNotFoundException("No specified entity found");
        }
        studentDao.deleteById(id);
        log.info("Successful student deletion");
    }

    @Override
    public void editStudent(Student student) {
        log.info("Student editing started");
        log.info("Checking student existence");
        Optional<Student> studentDb = studentDao.findById(student.getId());
        if (!studentDb.isPresent()) {
            log.error("Student to edit not found");
            throw new EntityNotFoundException("No specified entity found");
        }
        Student studentToEdit = studentDb.get();
        if (isEmailChanged(student, studentToEdit)) {
            log.info("Email has been changed, checking if same email already exists");
            if (studentDao.findByEmail(student.getEmail()).isPresent()) {
                log.error("Email already exists");
                throw new EntityAlreadyExistException("Specified email already exists");
            }
        }
        log.info("Validating student");
        validator.validate(student);
        studentDao.update(student);
        log.info("Student successfully edited");
    }

    protected boolean isEmailChanged(Student student, Student studentToEdit) {
        return !Objects.equals(studentToEdit.getEmail(), student.getEmail());
    }

    protected Page generatePage(int pageNumber) {
        return new Page(pageNumber, ENTITIES_ON_PAGE);
    }
}
