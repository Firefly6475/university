package ua.com.foxminded.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Group {    
    private final String id;
    private final List<Student> students;
    
    public Group(String id) {
        this.id = id;
        this.students = new ArrayList<>();
    }
    
    public void addStudent(Student student) {
        students.add(student);
    }

    public String getId() {
        return id;
    }

    public List<Student> getStudents() {
        return students;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, students);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Group other = (Group) obj;
        return Objects.equals(id, other.id) && Objects.equals(students, other.students);
    }

    @Override
    public String toString() {
        return "Group [id=" + id + ", students=" + students + "]";
    }
}
