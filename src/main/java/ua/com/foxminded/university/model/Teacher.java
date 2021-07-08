package ua.com.foxminded.university.model;

import java.util.Objects;

public class Teacher extends Person {    
    private Integer salary;
    
    public Teacher(String name, String birthday, Integer salary) {
        super(name, birthday);
        this.salary = salary;
    }

    public Integer getSalary() {
        return salary;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(salary);
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
        Teacher other = (Teacher) obj;
        return Objects.equals(salary, other.salary);
    }

    @Override
    public String toString() {
        return "Teacher [salary=" + salary + "]";
    }
}
