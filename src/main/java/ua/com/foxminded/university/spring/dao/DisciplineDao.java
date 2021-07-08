package ua.com.foxminded.university.spring.dao;

import ua.com.foxminded.university.model.Discipline;

public interface DisciplineDao extends CrudDao<Discipline> {
    void addTeacherToDiscipline(String disciplineId, String teacherId);
    
    void addAllTeachersToDiscipline(Discipline discipline);
    
    void removeTeacherFromDiscipline(String disciplineId, String teacherId);
}
