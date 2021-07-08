package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TeacherClass extends Class {
    private Group group;
    
    public TeacherClass(Builder builder) {
        super(builder.discipline, builder.audience, builder.classType, builder.date, builder.timeStart, builder.timeEnd);
        this.group = builder.group;
    }

    public Group getGroup() {
        return group;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder (TeacherClass teacherClass) {
        return new Builder();
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
    
    public static class Builder {
        private Discipline discipline;
        private Audience audience;
        private ClassType classType;
        private LocalDate date;
        private LocalTime timeStart;
        private LocalTime timeEnd;
        private Group group;
        
        private Builder() {
            
        }
        
        private Builder(TeacherClass teacherClass) {
            this.discipline = teacherClass.getDiscipline();
            this.audience = teacherClass.getAudience();
            this.classType = teacherClass.getClassType();
            this.date = teacherClass.getDate();
            this.timeStart = teacherClass.getTimeStart();
            this.timeEnd = teacherClass.getTimeEnd();
            this.group = teacherClass.getGroup();
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
        
        public Builder withGroup(Group group) {
            this.group = group;
            return this;
        }
        
        public TeacherClass build() {
            return new TeacherClass(this);
        }
    }
}
