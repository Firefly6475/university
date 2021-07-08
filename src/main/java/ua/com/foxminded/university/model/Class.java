package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public abstract class Class {
    private Discipline discipline;
    private Audience audience;
    private ClassType classType;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;

    protected Class(Discipline discipline, Audience audience, ClassType classType, LocalDate date,
            LocalTime timeStart, LocalTime timeEnd) {
        this.discipline = discipline;
        this.audience = audience;
        this.classType = classType;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
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
}
