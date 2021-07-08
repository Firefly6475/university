package ua.com.foxminded.university.model;

import java.util.Objects;

public class Student extends Person {    
    private Integer course;
    
    public Student(String name, String birthday) {
        super(name, birthday);
        this.course = 1;
    }

    public Integer getCourse() {
        return course;
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
        return "Student [course=" + course + "]";
    }    
}
