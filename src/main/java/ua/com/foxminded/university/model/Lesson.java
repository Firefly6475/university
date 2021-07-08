package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Lesson {
    private final String id;
    private final Discipline discipline;
    private final Audience audience;
    private final LessonType lessonType;
    private final Group group;
    private final Teacher teacher;
    private final LocalDate date;
    private final LocalTime timeStart;
    private final LocalTime timeEnd;

    protected Lesson(Builder builder) {
        this.id = builder.id;
        this.discipline = builder.discipline;
        this.audience = builder.audience;
        this.lessonType = builder.lessonType;
        this.group = builder.group;
        this.teacher = builder.teacher;
        this.date = builder.date;
        this.timeStart = builder.timeStart;
        this.timeEnd = builder.timeEnd;
    }

    public String getId() {
        return id;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public Audience getAudience() {
        return audience;
    }

    public LessonType getLessonType() {
        return lessonType;
    }
    
    public Group getGroup() {
        return group;
    }

    public Teacher getTeacher() {
        return teacher;
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
        return new Builder();
    }
    
    public static Builder builder (Lesson lesson) {
        return new Builder(lesson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(audience, date, discipline, group, id, lessonType, teacher, timeEnd,
                timeStart);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Lesson other = (Lesson) obj;
        return Objects.equals(audience, other.audience)
                && Objects.equals(date, other.date)
                && Objects.equals(discipline, other.discipline)
                && Objects.equals(group, other.group) && Objects.equals(id, other.id)
                && lessonType == other.lessonType && Objects.equals(teacher, other.teacher)
                && Objects.equals(timeEnd, other.timeEnd)
                && Objects.equals(timeStart, other.timeStart);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id='" + id + '\'' +
                ", discipline=" + discipline +
                ", audience=" + audience +
                ", lessonType=" + lessonType +
                ", group=" + group +
                ", teacher=" + teacher +
                ", date=" + date +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                '}';
    }

    public static class Builder {
        private String id;
        private Discipline discipline;
        private Audience audience;
        private LessonType lessonType;
        private Group group;
        private Teacher teacher;
        private LocalDate date;
        private LocalTime timeStart;
        private LocalTime timeEnd;
        
        protected Builder() {
            
        }
        
        protected Builder(Lesson lesson) {
            this.id = lesson.id;
            this.discipline = lesson.discipline;
            this.audience = lesson.audience;
            this.lessonType = lesson.lessonType;
            this.group = lesson.group;
            this.teacher = lesson.teacher;
            this.date = lesson.date;
            this.timeStart = lesson.timeStart;
            this.timeEnd = lesson.timeEnd;
        }
        
        public Builder withId(String id) {
            this.id = id;
            return this;
        }
        
        public Builder withDiscipline(Discipline discipline) {
            this.discipline = discipline;
            return this;
        }
        
        public Builder withAudience(Audience audience) {
            this.audience = audience;
            return this;
        }
        
        public Builder withLessonType(LessonType lessonType) {
            this.lessonType = lessonType;
            return this;
        }
        
        public Builder withGroup(Group group) {
            this.group = group;
            return this;
        }
        
        public Builder withTeacher(Teacher teacher) {
            this.teacher = teacher;
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
        
        public Lesson build() {
            return new Lesson(this); 
        }
    }
}
