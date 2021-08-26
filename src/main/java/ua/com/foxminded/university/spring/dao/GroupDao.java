package ua.com.foxminded.university.spring.dao;

import ua.com.foxminded.university.model.Group;

public interface GroupDao extends CrudDao<Group> {
    void addStudentToGroup(String groupId, String studentId);
    
    void addAllStudentsToGroup(Group group);
    
    void removeStudentFromGroup(String groupId, String studentId);
}
