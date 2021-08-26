package ua.com.foxminded.university.model;

import java.util.Objects;

public class Student extends Person {    
    private final Integer course;
    
    public Student(Builder builder) {
        super(builder);
        this.course = builder.course;
    }

    public Integer getCourse() {
        return course;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder(Student student) {
        return new Builder(student);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(course);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        Student other = (Student) obj;
        return Objects.equals(course, other.course);
    }

    @Override
    public String toString() {
        return "Student{" +
                "course=" + course +
                "} " + super.toString();
    }

    public static class Builder extends Person.Builder<Builder> {
        private Integer course;
        
        private Builder() {
            
        }
        
        private Builder(Student student) {
            this.course = student.course;
        }
        
        @Override
        public Builder getThis() {
            return this;
        }
        
        public Builder withCourse(Integer course) {
            this.course = course;
            return this;
        }
        
        @Override
        public Student build() {
            return new Student(this);
        }
    }
}
