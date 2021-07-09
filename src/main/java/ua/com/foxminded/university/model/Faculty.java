package ua.com.foxminded.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Faculty {    
    private final String name;
    private final List<Group> groups;
    
    public Faculty(String name) {
        this.name = name;
        this.groups = new ArrayList<>();
    }
    
    public void addGroup(Group group) {
        groups.add(group);
    }

    public String getName() {
        return name;
    }

    public List<Group> getGroups() {
        return groups;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Faculty other = (Faculty) obj;
        return Objects.equals(groups, other.groups) && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "Faculty [name=" + name + ", groups=" + groups + "]";
    }    
}
