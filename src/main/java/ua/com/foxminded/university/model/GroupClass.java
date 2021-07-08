package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class GroupClass extends Class {
    private Teacher teacher;
    
    private GroupClass(Builder builder) {
        super(builder.discipline, builder.audience, builder.classType, builder.date, builder.timeStart, builder.timeEnd);
        this.teacher = builder.teacher;
    }

    public Teacher getTeacher() {
        return teacher;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder (GroupClass groupClass) {
        return new Builder();
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
    
    public static class Builder {
        private Discipline discipline;
        private Audience audience;
        private ClassType classType;
        private LocalDate date;
        private LocalTime timeStart;
        private LocalTime timeEnd;
        private Teacher teacher;
        
        private Builder() {
            
        }
        
        private Builder(GroupClass groupClass) {
            this.discipline = groupClass.getDiscipline();
            this.audience = groupClass.getAudience();
            this.classType = groupClass.getClassType();
            this.date = groupClass.getDate();
            this.timeStart = groupClass.getTimeStart();
            this.timeEnd = groupClass.getTimeEnd();
            this.teacher = groupClass.getTeacher();
        }
        
        public Builder withDiscipline(Discipline discipline) {
            this.discipline = discipline;
            return this;
        }
        
        public Builder withAudience(Audience audience) {
            this.audience = audience;
            return this;
        }
        
        public Builder withClassType(ClassType classType) {
            this.classType = classType;
            return this;
        }
        
        public Builder withDate(LocalDate date) {
            this.date = date;
            return this;
        }
        
        public Builder withTimeStart(LocalTime timeStart) {
            this.timeStart = timeStart;
            return this;
        }
        
        public Builder withTimeEnd(LocalTime timeEnd) {
            this.timeEnd = timeEnd;
            return this;
        }
        
        public Builder withTeacher(Teacher teacher) {
            this.teacher = teacher;
            return this;
        }
        
        public GroupClass build() {
            return new GroupClass(this);
        }
    }
}
