package ua.com.foxminded.university.spring.dao;

import ua.com.foxminded.university.model.Group;

import java.util.Optional;

public interface GroupDao extends CrudDao<Group> {
    void addStudentToGroup(String groupId, String studentId);
    
    void addAllStudentsToGroup(Group group);
    
    void removeStudentFromGroup(String groupId, String studentId);

    Optional<Group> findByName(String name);
}
