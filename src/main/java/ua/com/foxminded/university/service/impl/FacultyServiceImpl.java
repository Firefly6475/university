package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FacultyServiceImpl implements FacultyService {
    private final FacultyDao facultyDao;
    private final GroupDao groupDao;
    private final Validator<Faculty> validator;

    @Override
    public void addFaculty(Faculty faculty) {
        log.info("Adding faculty started");
        log.info("Checking faculty name existence in database");
        if (facultyDao.findByName(faculty.getName()).isPresent()) {
            log.error("Faculty already exists in database");
            throw new EntityAlreadyExistException("Entity with specified name is already exists");
        }
        log.info("Validating faculty...");
        validator.validate(faculty);
        facultyDao.save(faculty);
        log.info("Faculty successfully added");
    }

    @Override
    public Faculty findFacultyById(String id) {
        log.info("Finding faculty by id in database started");
        return facultyDao.findById(id).orElseThrow(() -> {
            log.error("Faculty by id not found");
            return new EntityNotFoundException("No specified entity found");
        });
    }

    @Override
    public Faculty findFacultyByName(String name) {
        log.info("Finding faculty by name in database started");
        return facultyDao.findByName(name).orElseThrow(() -> {
            log.error("Faculty by name not found");
            return new EntityNotFoundException("No specified entity found");
        });

    }

    @Override
    public List<Faculty> showAllFaculties(Page page) {
        log.info("Getting all faculties started");
        return facultyDao.findAll(page);
    }

    @Override
    public void deleteFaculty(String id) {
        log.info("Faculty deletion by id started");
        log.info("Checking faculty existence");
        Optional<Faculty> facultyInDb = facultyDao.findById(id);
        if (!facultyInDb.isPresent()) {
            log.error("No faculty found for deletion");
            throw new EntityNotFoundException("No specified entity found");
        }
        log.info("Checking groups existence in faculty");
        if (!facultyInDb.get().getGroups().isEmpty()) {
            log.error("Faculty still has groups");
            throw new EntityIsNotEmptyException("Can't delete non-empty entity");
        }
        facultyDao.deleteById(id);
        log.info("Successful faculty deletion");
    }

    @Override
    public void editFaculty(Faculty faculty) {
        log.info("Faculty editing started");
        log.info("Checking faculty existence");
        Optional<Faculty> facultyInDb = facultyDao.findById(faculty.getId());
        if (!facultyInDb.isPresent()) {
            log.error("Faculty to edit not found");
            throw new EntityNotFoundException("No specified entity found");
        }
        log.info("Checking if the same name already exists");
        if (facultyDao.findByName(faculty.getName()).isPresent()) {
            log.error("Name already exists");
            throw new EntityAlreadyExistException("Specified name already exists");
        }
        log.info("Validating faculty...");
        validator.validate(faculty);
        facultyDao.update(faculty);
        log.info("Faculty successfully edited");
    }

    @Override
    public void addGroupToFaculty(String facultyId, String groupId) {
        log.info("Adding group to faculty started");
        log.info("Checking faculty existence");
        Optional<Faculty> facultyInDb = facultyDao.findById(facultyId);
        if (!facultyInDb.isPresent()) {
            log.error("Faculty to add group in not found");
            throw new EntityNotFoundException("Specified faculty not found");
        }
        log.info("Checking group existence");
        Optional<Group> groupInDb = groupDao.findById(groupId);
        if (!groupInDb.isPresent()) {
            log.error("Group to add not found");
            throw new EntityNotFoundException("Specified group not found");
        }
        log.info("Checking if group is already in a faculty");
        if (facultyInDb.get().getGroups().contains(groupInDb.get())) {
            log.error("Group is already in the faculty");
            throw new EntityAlreadyExistException("Specified group already in faculty");
        }
        facultyDao.addGroupToFaculty(facultyId, groupId);
        log.info("Successfully added group to faculty");
    }

    @Override
    public void removeGroupFromFaculty(String facultyId, String groupId) {
        log.info("Group from faculty removal started");
        log.info("Checking faculty existence");
        Optional<Faculty> facultyInDb = facultyDao.findById(facultyId);
        if (!facultyInDb.isPresent()) {
            log.error("Faculty to remove group from not found");
            throw new EntityNotFoundException("Specified faculty not found");
        }
        log.info("Checking group existence");
        Optional<Group> groupInDb = groupDao.findById(groupId);
        if (!groupInDb.isPresent()) {
            log.error("Group to remove from faculty not found");
            throw new EntityNotFoundException("Specified group not found");
        }
        log.info("Checking if group is in a faculty");
        if (!facultyInDb.get().getGroups().contains(groupInDb.get())) {
            log.error("No group in specified faculty");
            throw new EntityNotFoundException("Specified group is not in a faculty");
        }
        facultyDao.removeGroupFromFaculty(facultyId, groupId);
        log.info("Successfully removed group from faculty");
    }
}
