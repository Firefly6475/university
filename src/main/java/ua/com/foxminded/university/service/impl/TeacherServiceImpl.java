package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.Page;
import ua.com.foxminded.university.spring.dao.TeacherDao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherDao teacherDao;
    private final Validator<Teacher> validator;

    @Override
    public void registerTeacher(Teacher teacher) {
        log.info("Teacher registration started");
        log.info("Checking teacher email existence in database");
        if (teacherDao.findByEmail(teacher.getEmail()).isPresent()) {
            log.error("Teacher already exists in database");
            throw new EntityAlreadyExistException("Entity with specified email is already exists");
        }
        log.info("Validating teacher...");
        validator.validate(teacher);
        teacherDao.save(teacher);
        log.info("Teacher successfully saved");
    }

    @Override
    public List<Teacher> showAllTeachers(Page page) {
        log.info("Getting all teachers started");
        return teacherDao.findAll(page);
    }

    @Override
    public Teacher findTeacherById(String id) {
        log.info("Finding teacher by id in database started");
        return teacherDao.findById(id).orElseThrow(() -> {
            log.error("Teacher by id not found");
            return new EntityNotFoundException("No specified entity found");
        });
    }

    @Override
    public Teacher findTeacherByEmail(String email) {
        log.info("Finding teacher by email in database started");
        return teacherDao.findByEmail(email).orElseThrow(() -> {
            log.error("Teacher by email not found");
            return new EntityNotFoundException("No specified entity found");
        });
    }

    @Override
    public void deleteTeacher(String id) {
        log.info("Teacher deletion by id started");
        log.info("Checking teacher existence");
        if (!teacherDao.findById(id).isPresent()) {
            log.error("No teacher found for deletion");
            throw new EntityNotFoundException("No specified entity found");
        }
        teacherDao.deleteById(id);
        log.info("Successful teacher deletion");
    }

    @Override
    public void editTeacher(Teacher teacher) {
        log.info("Teacher editing started");
        log.info("Checking teacher existence");
        Optional<Teacher> teacherDb = teacherDao.findById(teacher.getId());
        if (!teacherDb.isPresent()) {
            log.error("Teacher to edit not found");
            throw new EntityNotFoundException("No specified entity found");
        }
        Teacher teacherToEdit = teacherDb.get();
        if (isEmailChanged(teacher, teacherToEdit)) {
            log.info("Email has been changed, checking if same email already exists");
            if (teacherDao.findByEmail(teacher.getEmail()).isPresent()) {
                log.error("Email already exists");
                throw new EntityAlreadyExistException("Specified email already exists");
            }
        }
        log.info("Validating teacher");
        validator.validate(teacher);
        teacherDao.update(teacher);
        log.info("Teacher successfully edited");
    }

    protected boolean isEmailChanged(Teacher teacher, Teacher teacherToEdit) {
        return !Objects.equals(teacherToEdit.getEmail(), teacher.getEmail());
    }
}
