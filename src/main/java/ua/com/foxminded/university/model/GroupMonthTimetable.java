package ua.com.foxminded.university.model;

import java.util.List;
import java.util.Objects;

public class GroupMonthTimetable {
    private List<GroupDayTimetable> classes;

    public GroupMonthTimetable(List<GroupDayTimetable> dayClasses) {
        this.classes = dayClasses;
    }

    public List<GroupDayTimetable> getClasses() {
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
        GroupMonthTimetable other = (GroupMonthTimetable) obj;
        return Objects.equals(classes, other.classes);
    }

    @Override
    public String toString() {
        return "GroupMonthTimetable [classes=" + classes + "]";
    }  
}
