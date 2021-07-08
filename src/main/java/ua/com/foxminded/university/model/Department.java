package ua.com.foxminded.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Department {    
    private String name;
    private List<Teacher> teachers;
    
    public Department(String name) {
        this.name = name;
        this.teachers = new ArrayList<>();
    }
    
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public String getName() {
        return name;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, teachers);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Department other = (Department) obj;
        return Objects.equals(name, other.name) && Objects.equals(teachers, other.teachers);
    }

    @Override
    public String toString() {
        return "Department [name=" + name + ", teachers=" + teachers + "]";
    }
}
