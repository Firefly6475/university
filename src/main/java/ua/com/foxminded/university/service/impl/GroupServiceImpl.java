package ua.com.foxminded.university.service.impl;

import lombok.RequiredArgsConstructor;
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
public class GroupServiceImpl implements GroupService {
    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final Validator<Group> validator;

    @Override
    public void addGroup(Group group) {
        if (groupDao.findByName(group.getName()).isPresent()) {
            throw new EntityAlreadyExistException("Entity with specified name is already exists");
        }
        validator.validate(group);
        groupDao.save(group);
    }

    @Override
    public Group findGroupById(String id) {
        return groupDao.findById(id).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));
    }

    @Override
    public Group findGroupByName(String name) {
        return groupDao.findByName(name).orElseThrow(() -> new EntityNotFoundException("No specified entity found"));
    }

    @Override
    public List<Group> showAllGroups(Page page) {
        return groupDao.findAll(page);
    }

    @Override
    public void deleteGroup(String id) {
        Optional<Group> groupInDb = groupDao.findById(id);
        if (!groupInDb.isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        if (!groupInDb.get().getStudents().isEmpty()) {
            throw new EntityIsNotEmptyException("Can't delete non-empty entity");
        }
        groupDao.deleteById(id);
    }

    @Override
    public void editGroup(Group group) {
        Optional<Group> groupInDb = groupDao.findById(group.getId());
        if (!groupInDb.isPresent()) {
            throw new EntityNotFoundException("No specified entity found");
        }
        Group groupToEdit = groupInDb.get();
        if (isNameChanged(group, groupToEdit)) {
            if (groupDao.findByName(group.getName()).isPresent()) {
                throw new EntityAlreadyExistException("Specified name already exists");
            }
        }
        validator.validate(group);
        groupDao.update(group);
    }

    @Override
    public void addStudentToGroup(String groupId, String studentId) {
        Optional<Group> groupInDb = groupDao.findById(groupId);
        Optional<Student> studentInDb = studentDao.findById(studentId);
        if (!groupInDb.isPresent()) {
            throw new EntityNotFoundException("Specified group not found");
        }
        if (!studentInDb.isPresent()) {
            throw new EntityNotFoundException("Specified student not found");
        }
        if (groupInDb.get().getStudents().contains(studentInDb.get())) {
            throw new EntityAlreadyExistException("Specified student already in group");
        }
        groupDao.addStudentToGroup(groupId, studentId);
    }

    @Override
    public void removeStudentFromGroup(String groupId, String studentId) {
        Optional<Group> groupInDb = groupDao.findById(groupId);
        Optional<Student> studentInDb = studentDao.findById(studentId);
        if (!groupInDb.isPresent()) {
            throw new EntityNotFoundException("Specified group not found");
        }
        if (!studentInDb.isPresent()) {
            throw new EntityNotFoundException("Specified student not found");
        }
        if (!groupInDb.get().getStudents().contains(studentInDb.get())) {
            throw new EntityNotFoundException("Specified student is not in a group");
        }
        groupDao.removeStudentFromGroup(groupId, studentId);
    }

    @Override
    public void addAllStudentsToGroup(Group group) {
        if (!groupDao.findById(group.getId()).isPresent()) {
            throw new EntityNotFoundException("Entity do not exists");
        }
        groupDao.addAllStudentsToGroup(group);
    }

    protected boolean isNameChanged(Group group, Group groupToEdit) {
        return !Objects.equals(groupToEdit.getName(), group.getName());
    }
}
