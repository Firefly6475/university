package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityIsNotEmptyException;
import ua.com.foxminded.university.service.exception.EntityNotFoundException;
import ua.com.foxminded.university.service.validator.Validator;
import ua.com.foxminded.university.spring.dao.GroupDao;
import ua.com.foxminded.university.spring.dao.Page;
import ua.com.foxminded.university.spring.dao.StudentDao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService {
    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final Validator<Group> validator;

    @Override
    public void addGroup(Group group) {
        log.info("Adding group started");
        log.info("Checking group existence in database");
        if (groupDao.findByName(group.getName()).isPresent()) {
            log.error("Group already exists in database");
            throw new EntityAlreadyExistException("Entity with specified name is already exists");
        }
        log.info("Validating group...");
        validator.validate(group);
        groupDao.save(group);
        log.info("Group successfully added");
    }

    @Override
    public Group findGroupById(String id) {
        log.info("Finding group by id in database started");
        return groupDao.findById(id).orElseThrow(() -> {
            log.error("Group by id not found");
            return new EntityNotFoundException("No specified entity found");
        });
    }

    @Override
    public Group findGroupByName(String name) {
        log.info("Finding group by name in database started");
        return groupDao.findByName(name).orElseThrow(() -> {
            log.error("Group by name not found");
            return new EntityNotFoundException("No specified entity found");
        });
    }

    @Override
    public List<Group> showAllGroups(Page page) {
        log.info("Getting all groups started");
        return groupDao.findAll(page);
    }

    @Override
    public void deleteGroup(String id) {
        log.info("Group deletion by id started");
        log.info("Checking group existence");
        Optional<Group> groupInDb = groupDao.findById(id);
        if (!groupInDb.isPresent()) {
            log.error("No group found for deletion");
            throw new EntityNotFoundException("No specified entity found");
        }
        log.info("Checking students existence in group");
        if (!groupInDb.get().getStudents().isEmpty()) {
            log.error("Group still has students");
            throw new EntityIsNotEmptyException("Can't delete non-empty entity");
        }
        groupDao.deleteById(id);
        log.info("Successful group deletion");
    }

    @Override
    public void editGroup(Group group) {
        log.info("Group editing started");
        log.info("Checking group existence");
        Optional<Group> groupInDb = groupDao.findById(group.getId());
        if (!groupInDb.isPresent()) {
            log.error("Group to edit not found");
            throw new EntityNotFoundException("No specified entity found");
        }
        Group groupToEdit = groupInDb.get();
        if (isNameChanged(group, groupToEdit)) {
            log.info("Group name has been changed, checking if same name already exists");
            if (groupDao.findByName(group.getName()).isPresent()) {
                log.error("Name already exists");
                throw new EntityAlreadyExistException("Specified name already exists");
            }
        }
        log.info("Validating group...");
        validator.validate(group);
        groupDao.update(group);
        log.info("Group successfully edited");
    }

    @Override
    public void addStudentToGroup(String groupId, String studentId) {
        log.info("Adding student to group started");
        log.info("Checking group existence");
        Optional<Group> groupInDb = groupDao.findById(groupId);
        if (!groupInDb.isPresent()) {
            log.error("Group to add student in not found");
            throw new EntityNotFoundException("Specified group not found");
        }
        log.info("Checking student existence");
        Optional<Student> studentInDb = studentDao.findById(studentId);
        if (!studentInDb.isPresent()) {
            log.error("Student to add not found");
            throw new EntityNotFoundException("Specified student not found");
        }
        log.info("Checking if student is already in a group");
        if (groupInDb.get().getStudents().contains(studentInDb.get())) {
            log.error("Student is already in the group");
            throw new EntityAlreadyExistException("Specified student already in group");
        }
        groupDao.addStudentToGroup(groupId, studentId);
        log.info("Successfully added student to group");
    }

    @Override
    public void removeStudentFromGroup(String groupId, String studentId) {
        log.info("Student from group removal started");
        log.info("Checking group existence");
        Optional<Group> groupInDb = groupDao.findById(groupId);
        if (!groupInDb.isPresent()) {
            log.error("Group to remove student from not found");
            throw new EntityNotFoundException("Specified group not found");
        }
        log.info("Checking student existence");
        Optional<Student> studentInDb = studentDao.findById(studentId);
        if (!studentInDb.isPresent()) {
            log.error("Student to remove from group not found");
            throw new EntityNotFoundException("Specified student not found");
        }
        log.info("Checking if student is in a group");
        if (!groupInDb.get().getStudents().contains(studentInDb.get())) {
            log.error("No student in specified group ");
            throw new EntityNotFoundException("Specified student is not in a group");
        }
        groupDao.removeStudentFromGroup(groupId, studentId);
        log.info("Successfully removed student from group");
    }

    protected boolean isNameChanged(Group group, Group groupToEdit) {
        return !Objects.equals(groupToEdit.getName(), group.getName());
    }
}
