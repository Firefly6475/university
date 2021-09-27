package ua.com.foxminded.university.spring.dao;

import ua.com.foxminded.university.model.Faculty;

import java.util.Optional;

public interface FacultyDao extends CrudDao<Faculty> {
    void addGroupToFaculty(String facultyId, String groupId);

    void addAllGroupsToFaculty(Faculty faculty);

    void removeGroupFromFaculty(String facultyId, String groupId);

    Optional<Faculty> findByName(String name);
}
