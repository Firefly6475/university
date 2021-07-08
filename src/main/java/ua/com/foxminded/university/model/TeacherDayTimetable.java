package ua.com.foxminded.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeacherDayTimetable {
    private List<TeacherClass> classes;
    
    public TeacherDayTimetable() {
        this.classes = new ArrayList<>();
    }
    
    public void addClass(TeacherClass teacherClass) {
        classes.add(teacherClass);
    }

    public List<TeacherClass> getClasses() {
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
        TeacherDayTimetable other = (TeacherDayTimetable) obj;
        return Objects.equals(classes, other.classes);
    }

    @Override
    public String toString() {
        return "TeacherDayTimetable [classes=" + classes + "]";
    }
}
