package ua.com.foxminded.university.model;

import java.util.Objects;

public class Teacher extends Person {    
    private final Integer salary;
    
    public Teacher(Builder builder) {
        super(builder);
        this.salary = builder.salary;
    }

    public Integer getSalary() {
        return salary;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder(Teacher teacher) {
        return new Builder(teacher);
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
    
    public static class Builder extends Person.Builder<Builder> {
        private Integer salary;
        
        private Builder() {
            
        }
        
        private Builder(Teacher teacher) {
            this.salary = teacher.salary;
        }
        
        @Override
        public Builder getThis() {
            return this;
        }
        
        public Builder withSalary(Integer salary) {
            this.salary = salary;
            return this;
        }
        
        @Override
        public Teacher build() {
            return new Teacher(this);
        }
    }
}
