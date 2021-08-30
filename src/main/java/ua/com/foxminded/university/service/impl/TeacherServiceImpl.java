package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherDao teacherDao;
    private final Validator<Teacher> validator;

    @Override
    public void registerTeacher(Teacher teacher) {
        if (teacherDao.findByEmail(teacher.getEmail()).isPresent()) {
            throw new EntityAlreadyExistException("Entity with specified email is already exists");
        }
        validator.validate(teacher);
        teacherDao.save(teacher);
    }

    @Override
    public List<Teacher> showAllTeachers(Page page) {
        return teacherDao.findAll(page);
    }

    @Override
    public Teacher findTeacherById(String id) {
        return teacherDao.findById(id).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));
    }

    @Override
    public Teacher findTeacherByEmail(String email) {
        return teacherDao.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));
    }

    @Override
    public void deleteTeacher(String id) {
        if (!teacherDao.findById(id).isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        teacherDao.deleteById(id);
    }

    @Override
    public void editTeacher(Teacher teacher) {
        Optional<Teacher> teacherDb = teacherDao.findById(teacher.getId());
        if (!teacherDb.isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        Teacher teacherToEdit = teacherDb.get();
        if (isEmailChanged(teacher, teacherToEdit)) {
            if (teacherDao.findByEmail(teacher.getEmail()).isPresent()) {
                throw new EntityAlreadyExistException("Specified email already exists");
            }
        }
        validator.validate(teacher);
        teacherDao.update(teacher);
    }

    protected boolean isEmailChanged(Teacher teacher, Teacher teacherToEdit) {
        return !Objects.equals(teacherToEdit.getEmail(), teacher.getEmail());
    }
}
