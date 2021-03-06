package ua.com.foxminded.university.service;

import ua.com.foxminded.university.model.Discipline;

import java.util.List;

public interface DisciplineService {
    void addDiscipline(Discipline discipline);

    Discipline findDisciplineById(String id);

    Discipline findDisciplineByName(String name);

    List<Discipline> showAllDisciplines();

    void deleteDiscipline(String id);

    void editDiscipline(Discipline discipline);

    void addTeacherToDiscipline(String disciplineId, String teacherId);

    void removeTeacherFromDiscipline(String disciplineId, String teacherId);
}
