package ua.com.foxminded.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupDayTimetable {
    private List<GroupClass> classes;
    
    public GroupDayTimetable() {
        this.classes = new ArrayList<>();
    }
    
    public void addClass(GroupClass groupClass) {
        classes.add(groupClass);
    }

    public List<GroupClass> getClasses() {
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
        GroupDayTimetable other = (GroupDayTimetable) obj;
        return Objects.equals(classes, other.classes);
    }

    @Override
    public String toString() {
        return "GroupDayTimetable [classes=" + classes + "]";
    }
}
