package ua.com.foxminded.university.spring.dao;

import ua.com.foxminded.university.model.Faculty;

public interface FacultyDao extends CrudDao<Faculty> {
    void addGroupToFaculty(String facultyId, String groupId);
    
    void addAllGroupsToFaculty(Faculty faculty);
    
    void removeGroupFromFaculty(String facultyId, String groupId);
}
