package ua.com.foxminded.university.model;

import java.util.List;
import java.util.Objects;

public class TeacherMonthTimetable {
    private List<TeacherDayTimetable> classes;
    
    public TeacherMonthTimetable(List<TeacherDayTimetable> dayClasses) {
        this.classes = dayClasses;
    }

    public List<TeacherDayTimetable> getClasses() {
        return classes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(classes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TeacherMonthTimetable other = (TeacherMonthTimetable) obj;
        return Objects.equals(classes, other.classes);
    }

    @Override
    public String toString() {
        return "TeacherMonthTimetable [classes=" + classes + "]";
    }
    
    
}
