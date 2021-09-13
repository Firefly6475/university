package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.spring.dao.Page;

import java.util.List;

public interface GroupService {
    void addGroup(Group group);

    Group findGroupById(String id);

    Group findGroupByName(String name);

    List<Group> showAllGroups(Page page);

    void deleteGroup(String id);

    void editGroup(Group group);

    void addStudentToGroup(String groupId, String studentId);

    void removeStudentFromGroup(String groupId, String studentId);

    void addAllStudentsToGroup(Group group);
}
