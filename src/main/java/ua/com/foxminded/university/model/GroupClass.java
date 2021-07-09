package ua.com.foxminded.university.model;

import java.util.Objects;

public class GroupClass extends Class {
    private final Teacher teacher;
    
    private GroupClass(Builder builder) {
        super(builder);
        this.teacher = builder.teacher;
    }

    public Teacher getTeacher() {
        return teacher;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder (GroupClass groupClass) {
        return new Builder(groupClass);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(teacher);
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
        GroupClass other = (GroupClass) obj;
        return Objects.equals(teacher, other.teacher);
    }

    @Override
    public String toString() {
        return "GroupClass [teacher=" + teacher + "]";
    }
    
    public static class Builder extends Class.Builder<Builder> {
        private Teacher teacher;
        
        private Builder() {
            
        }
        
        private Builder(GroupClass groupClass) {
            this.teacher = groupClass.teacher;
        }
        
        @Override
        public Builder getThis() {
            return this;
        }
        
        public Builder withTeacher(Teacher teacher) {
            this.teacher = teacher;
            return this;
        }
        
        @Override
        public GroupClass build() {
            return new GroupClass(this);
        }
    }
}
