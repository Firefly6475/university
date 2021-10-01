package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DisciplineServiceImpl implements DisciplineService {
    private final DisciplineDao disciplineDao;
    private final TeacherDao teacherDao;
    private final Validator<Discipline> validator;

    @Override
    public void addDiscipline(Discipline discipline) {
        log.info("Adding discipline started");
        log.info("Checking discipline name existence in database");
        if (disciplineDao.findByName(discipline.getName()).isPresent()) {
            log.error("Discipline already exists in database");
            throw new EntityAlreadyExistException("Entity with specified name is already exists");
        }
        log.info("Validating discipline...");
        validator.validate(discipline);
        disciplineDao.save(discipline);
        log.info("Discipline successfully added");
    }

    @Override
    public Discipline findDisciplineById(String id) {
        log.info("Finding discipline by id in database started");
        return disciplineDao.findById(id).orElseThrow(() -> {
            log.error("Discipline by id not found");
            return new EntityNotFoundException("No specified entity found");
        });
    }

    @Override
    public Discipline findDisciplineByName(String name) {
        log.info("Finding discipline by name in database started");
        return disciplineDao.findByName(name).orElseThrow(() -> {
            log.error("Discipline by name not found");
            return new EntityNotFoundException("No specified entity found");
        });

    }

    @Override
    public List<Discipline> showAllDisciplines(Page page) {
        log.info("Getting all disciplines started");
        return disciplineDao.findAll(page);
    }

    @Override
    public void deleteDiscipline(String id) {
        log.info("Discipline deletion by id started");
        log.info("Checking discipline existence");
        Optional<Discipline> disciplineInDb = disciplineDao.findById(id);
        if (!disciplineInDb.isPresent()) {
            log.error("No discipline found for deletion");
            throw new EntityNotFoundException("No specified entity found");
        }
        log.info("Checking teachers existence in discipline");
        if (!disciplineInDb.get().getTeachers().isEmpty()) {
            log.error("Discipline still has teachers");
            throw new EntityIsNotEmptyException("Can't delete non-empty entity");
        }
        disciplineDao.deleteById(id);
        log.info("Successful discipline deletion");
    }

    @Override
    public void editDiscipline(Discipline discipline) {
        log.info("Discipline editing started");
        log.info("Checking discipline existence");
        Optional<Discipline> disciplineInDb = disciplineDao.findById(discipline.getId());
        if (!disciplineInDb.isPresent()) {
            log.error("Discipline to edit not found");
            throw new EntityNotFoundException("No specified entity found");
        }
        log.info("Checking if the same name already exists");
        if (disciplineDao.findByName(discipline.getName()).isPresent()) {
            log.error("Name already exists");
            throw new EntityAlreadyExistException("Specified name already exists");
        }
        log.info("Validating discipline...");
        validator.validate(discipline);
        disciplineDao.update(discipline);
        log.info("Discipline successfully edited");
    }

    @Override
    public void addTeacherToDiscipline(String disciplineId, String teacherId) {
        log.info("Adding teacher to discipline started");
        log.info("Checking discipline existence");
        Optional<Discipline> disciplineInDb = disciplineDao.findById(disciplineId);
        if (!disciplineInDb.isPresent()) {
            log.error("Discipline to add teacher in not found");
            throw new EntityNotFoundException("Specified discipline not found");
        }
        log.info("Checking teacher existence");
        Optional<Teacher> teacherInDb = teacherDao.findById(teacherId);
        if (!teacherInDb.isPresent()) {
            log.error("Teacher to add not found");
            throw new EntityNotFoundException("Specified teacher not found");
        }
        log.info("Checking if teacher is already in a discipline");
        if (disciplineInDb.get().getTeachers().contains(teacherInDb.get())) {
            log.error("Teacher is already in the discipline");
            throw new EntityAlreadyExistException("Specified teacher already in discipline");
        }
        disciplineDao.addTeacherToDiscipline(disciplineId, teacherId);
        log.info("Successfully added teacher to discipline");
    }

    @Override
    public void removeTeacherFromDiscipline(String disciplineId, String teacherId) {
        log.info("Teacher from discipline removal started");
        log.info("Checking discipline existence");
        Optional<Discipline> disciplineInDb = disciplineDao.findById(disciplineId);
        if (!disciplineInDb.isPresent()) {
            log.error("Discipline to remove teacher from not found");
            throw new EntityNotFoundException("Specified discipline not found");
        }
        log.info("Checking teacher existence");
        Optional<Teacher> teacherInDb = teacherDao.findById(teacherId);
        if (!teacherInDb.isPresent()) {
            log.error("Teacher to remove from discipline not found");
            throw new EntityNotFoundException("Specified teacher not found");
        }
        log.info("Checking if teacher is in a discipline");
        if (!disciplineInDb.get().getTeachers().contains(teacherInDb.get())) {
            log.error("No teacher in specified discipline ");
            throw new EntityNotFoundException("Specified teacher is not in a discipline");
        }
        disciplineDao.removeTeacherFromDiscipline(disciplineId, teacherId);
        log.info("Successfully removed teacher from discipline");
    }
}
