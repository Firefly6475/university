package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.FacultyService;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityIsNotEmptyException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.FacultyDao;
import ua.com.foxminded.university.spring.dao.GroupDao;
import ua.com.foxminded.university.spring.dao.Page;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {
    private final FacultyDao facultyDao;
    private final GroupDao groupDao;
    private final Validator<Faculty> validator;

    @Override
    public void addFaculty(Faculty faculty) {
        if (facultyDao.findByName(faculty.getName()).isPresent()) {
            throw new EntityAlreadyExistException("Entity with specified name is already exists");
        }
        validator.validate(faculty);
        facultyDao.save(faculty);
    }

    @Override
    public Faculty findFacultyById(String id) {
        return facultyDao.findById(id).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));
    }

    @Override
    public Faculty findFacultyByName(String name) {
        return facultyDao.findByName(name).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));

    }

    @Override
    public List<Faculty> showAllFaculties(Page page) {
        return facultyDao.findAll(page);
    }

    @Override
    public void deleteFaculty(String id) {
        Optional<Faculty> facultyInDb = facultyDao.findById(id);
        if (!facultyInDb.isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        if (!facultyInDb.get().getGroups().isEmpty()) {
            throw new EntityIsNotEmptyException("Can't delete non-empty entity");
        }
        facultyDao.deleteById(id);
    }

    @Override
    public void editFaculty(Faculty faculty) {
        Optional<Faculty> facultyInDb = facultyDao.findById(faculty.getId());
        if (!facultyInDb.isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        if (facultyDao.findByName(faculty.getName()).isPresent()) {
            throw new EntityAlreadyExistException("Specified name already exists");
        }
        validator.validate(faculty);
        facultyDao.update(faculty);
    }

    @Override
    public void addGroupToFaculty(String facultyId, String groupId) {
        Optional<Faculty> facultyInDb = facultyDao.findById(facultyId);
        if (!facultyInDb.isPresent()) {
            throw new EntityNotFoundException("Specified faculty not found");
        }

        Optional<Group> groupInDb = groupDao.findById(groupId);
        if (!groupInDb.isPresent()) {
            throw new EntityNotFoundException("Specified group not found");
        }

        if (facultyInDb.get().getGroups().contains(groupInDb.get())) {
            throw new EntityAlreadyExistException("Specified group already in faculty");
        }
        facultyDao.addGroupToFaculty(facultyId, groupId);
    }

    @Override
    public void removeGroupFromFaculty(String facultyId, String groupId) {
        Optional<Faculty> facultyInDb = facultyDao.findById(facultyId);
        if (!facultyInDb.isPresent()) {
            throw new EntityNotFoundException("Specified faculty not found");
        }

        Optional<Group> groupInDb = groupDao.findById(groupId);
        if (!groupInDb.isPresent()) {
            throw new EntityNotFoundException("Specified group not found");
        }

        if (!facultyInDb.get().getGroups().contains(groupInDb.get())) {
            throw new EntityNotFoundException("Specified group is not in a faculty");
        }

        facultyDao.removeGroupFromFaculty(facultyId, groupId);
    }
}
