package ua.com.foxminded.university.model;

import java.util.List;
import java.util.Objects;

public class Discipline {
    private final String id;
    private final String name;
    private final List<Teacher> teachers;
    
    public Discipline(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.teachers = builder.teachers;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }
    
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder (Discipline discipline) {
        return new Builder(discipline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, teachers);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Discipline other = (Discipline) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(teachers, other.teachers);
    }

    @Override
    public String toString() {
        return "Discipline [id=" + id + ", name=" + name + ", teachers=" + teachers + "]";
    }
    
    public static class Builder {
        private String id;
        private String name;
        private List<Teacher> teachers;
        
        private Builder() {
            
        }
        
        private Builder (Discipline discipline) {
            this.id = discipline.id;
            this.name = discipline.name;
            this.teachers = discipline.teachers;
        }
        
        public Builder withId(String id) {
            this.id = id;
            return this;
        }
        
        public Builder withName(String name) {
            this.name = name;
            return this;
        }
        
        public Builder withTeachers(List<Teacher> teachers) {
            this.teachers = teachers;
            return this;
        }
        
        public Discipline build() {
            return new Discipline(this);
        }
    }
}
