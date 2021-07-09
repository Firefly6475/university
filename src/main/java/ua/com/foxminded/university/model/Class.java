package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Class {
    private final Discipline discipline;
    private final Audience audience;
    private final ClassType classType;
    private final LocalDate date;
    private final LocalTime timeStart;
    private final LocalTime timeEnd;

    protected Class(Builder<?> builder) {
        this.discipline = builder.discipline;
        this.audience = builder.audience;
        this.classType = builder.classType;
        this.date = builder.date;
        this.timeStart = builder.timeStart;
        this.timeEnd = builder.timeEnd;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public Audience getAudience() {
        return audience;
    }

    public ClassType getClassType() {
        return classType;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }
    
    public static Builder builder() {
        return new Builder() {
            @Override
            public Builder getThis() {
                return this;
            }
        };
    }
    
    public static Builder builder (Class thisClass) {
        return new Builder(thisClass) {
            @Override
            public Builder getThis() {
                return this;
            }
        };
    }

    @Override
    public int hashCode() {
        return Objects.hash(audience, classType, date, discipline, timeEnd, timeStart);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Class other = (Class) obj;
        return Objects.equals(audience, other.audience) && classType == other.classType
                && Objects.equals(date, other.date) && Objects.equals(discipline, other.discipline)
                && Objects.equals(timeEnd, other.timeEnd)
                && Objects.equals(timeStart, other.timeStart);
    }

    @Override
    public String toString() {
        return "Class [discipline=" + discipline + ", audience=" + audience + ", classType="
                + classType + ", date=" + date + ", timeStart=" + timeStart + ", timeEnd=" + timeEnd
                + "]";
    }
    
    public abstract static class Builder<T extends Builder<T>> {
        private Discipline discipline;
        private Audience audience;
        private ClassType classType;
        private LocalDate date;
        private LocalTime timeStart;
        private LocalTime timeEnd;
        
        protected Builder() {
            
        }
        
        protected Builder(Class myClass) {
            this.discipline = myClass.discipline;
            this.audience = myClass.audience;
            this.classType = myClass.classType;
            this.date = myClass.date;
            this.timeStart = myClass.timeStart;
            this.timeEnd = myClass.timeEnd;
        }
        
        public abstract T getThis();
        
        public T withDiscipline(Discipline discipline) {
            this.discipline = discipline;
            return this.getThis();
        }
        
        public T withAudience(Audience audience) {
            this.audience = audience;
            return this.getThis();
        }
        
        public T withClassType(ClassType classType) {
            this.classType = classType;
            return this.getThis();
        }
        
        public T withDate(LocalDate date) {
            this.date = date;
            return this.getThis();
        }
        
        public T withTimeStart(LocalTime timeStart) {
            this.timeStart = timeStart;
            return this.getThis();
        }
        
        public Class build() {
            return new Class(this); 
        }
    }
}
