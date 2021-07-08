package ua.com.foxminded.university.model;

import java.util.List;
import java.util.Objects;

public class Group {    
    private final String id;
    private final String name;
    private final List<Student> students;
    
    public Group(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.students = builder.students;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public List<Student> getStudents() {
        return students;
    }
    
    public void addStudent(Student student) {
        students.add(student);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder(Group group) {
        return new Builder(group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, students);
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
        return Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(students, other.students);
    }

    @Override
    public String toString() {
        return "Group [id=" + id + ", name=" + name + ", students=" + students + "]";
    }
    
    public static class Builder {
        private String id;
        private String name;
        private List<Student> students;
        
        private Builder() {
            
        }
        
        private Builder (Group group) {
            this.id = group.id;
            this.name = group.name;
            this.students = group.students;
        }
        
        public Builder withId(String id) {
            this.id = id;
            return this;
        }
        
        public Builder withName(String name) {
            this.name = name;
            return this;
        }
        
        public Builder withStudents(List<Student> students) {
            this.students = students;
            return this;
        }
        
        public Group build() {
            return new Group(this);
        }
    }
}
