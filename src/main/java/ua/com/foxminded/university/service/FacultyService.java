package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Faculty;
import ua.com.foxminded.university.spring.dao.Page;

import java.util.List;

public interface FacultyService {
    void addFaculty(Faculty faculty);

    Faculty findFacultyById(String id);

    Faculty findFacultyByName(String name);

    List<Faculty> showAllFaculties(Page page);

    void deleteFaculty(String id);

    void editFaculty(Faculty faculty);

    void addGroupToFaculty(String facultyId, String groupId);

    void removeGroupFromFaculty(String facultyId, String groupId);
}
