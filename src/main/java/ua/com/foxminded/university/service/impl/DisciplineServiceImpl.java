package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.model.Discipline;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.DisciplineService;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityIsNotEmptyException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.DisciplineDao;
import ua.com.foxminded.university.spring.dao.Page;
import ua.com.foxminded.university.spring.dao.TeacherDao;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DisciplineServiceImpl implements DisciplineService {
    private final DisciplineDao disciplineDao;
    private final TeacherDao teacherDao;
    private final Validator<Discipline> validator;

    @Override
    public void addDiscipline(Discipline discipline) {
        if (disciplineDao.findByName(discipline.getName()).isPresent()) {
            throw new EntityAlreadyExistException("Entity with specified name is already exists");
        }
        validator.validate(discipline);
        disciplineDao.save(discipline);
    }

    @Override
    public Discipline findDisciplineById(String id) {
        return disciplineDao.findById(id).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));
    }

    @Override
    public Discipline findDisciplineByName(String name) {
        return disciplineDao.findByName(name).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));

    }

    @Override
    public List<Discipline> showAllDisciplines(Page page) {
        return disciplineDao.findAll(page);
    }

    @Override
    public void deleteDiscipline(String id) {
        Optional<Discipline> disciplineInDb = disciplineDao.findById(id);
        if (!disciplineInDb.isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        if (!disciplineInDb.get().getTeachers().isEmpty()) {
            throw new EntityIsNotEmptyException("Can't delete non-empty entity");
        }
        disciplineDao.deleteById(id);
    }

    @Override
    public void editDiscipline(Discipline discipline) {
        Optional<Discipline> disciplineInDb = disciplineDao.findById(discipline.getId());
        if (!disciplineInDb.isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        if (disciplineDao.findByName(discipline.getName()).isPresent()) {
            throw new EntityAlreadyExistException("Specified name already exists");
        }
        validator.validate(discipline);
        disciplineDao.update(discipline);
    }

    @Override
    public void addTeacherToDiscipline(String disciplineId, String teacherId) {
        Optional<Discipline> disciplineInDb = disciplineDao.findById(disciplineId);
        if (!disciplineInDb.isPresent()) {
            throw new EntityNotFoundException("Specified discipline not found");
        }

        Optional<Teacher> teacherInDb = teacherDao.findById(teacherId);
        if (!teacherInDb.isPresent()) {
            throw new EntityNotFoundException("Specified teacher not found");
        }

        if (disciplineInDb.get().getTeachers().contains(teacherInDb.get())) {
            throw new EntityAlreadyExistException("Specified teacher already in discipline");
        }
        disciplineDao.addTeacherToDiscipline(disciplineId, teacherId);
    }

    @Override
    public void removeTeacherFromDiscipline(String disciplineId, String teacherId) {
        Optional<Discipline> disciplineInDb = disciplineDao.findById(disciplineId);
        if (!disciplineInDb.isPresent()) {
            throw new EntityNotFoundException("Specified discipline not found");
        }

        Optional<Teacher> teacherInDb = teacherDao.findById(teacherId);
        if (!teacherInDb.isPresent()) {
            throw new EntityNotFoundException("Specified teacher not found");
        }

        if (!disciplineInDb.get().getTeachers().contains(teacherInDb.get())) {
            throw new EntityNotFoundException("Specified teacher is not in a discipline");
        }

        disciplineDao.removeTeacherFromDiscipline(disciplineId, teacherId);
    }
}
