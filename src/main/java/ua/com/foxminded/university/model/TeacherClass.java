package ua.com.foxminded.university.model;

import java.util.Objects;

public class TeacherClass extends Class {
    private final Group group;
    
    public TeacherClass(Builder builder) {
        super(builder);
        this.group = builder.group;
    }

    public Group getGroup() {
        return group;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder (TeacherClass teacherClass) {
        return new Builder(teacherClass);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(group);
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
        TeacherClass other = (TeacherClass) obj;
        return Objects.equals(group, other.group);
    }

    @Override
    public String toString() {
        return "TeacherClass [group=" + group + "]";
    }
    
    public static class Builder extends Class.Builder<Builder> {
        private Group group;
        
        private Builder() {
            
        }
        
        private Builder(TeacherClass teacherClass) {
            this.group = teacherClass.group;
        }
        
        @Override
        public Builder getThis() {
            return this;
        }
        
        public Builder withGroup(Group group) {
            this.group = group;
            return this;
        }
        
        @Override
        public TeacherClass build() {
            return new TeacherClass(this);
        }
    }
}
