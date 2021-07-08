package ua.com.foxminded.university.model;

import java.util.List;
import java.util.Objects;

public class Faculty {
    private final String id;
    private final String name;
    private final List<Group> groups;
    
    public Faculty(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.groups = builder.groups;
    }
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Group> getGroups() {
        return groups;
    }
    
    public void addGroup(Group group) {
        groups.add(group);
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder (Faculty faculty) {
        return new Builder(faculty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups, id, name);
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
        return Objects.equals(groups, other.groups)
                && Objects.equals(id, other.id)
                && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "Faculty [id=" + id + ", name=" + name + ", groups=" + groups + "]";
    }    
    
    public static class Builder {
        private String id;
        private String name;
        private List<Group> groups;
        
        private Builder() {
            
        }
        
        private Builder (Faculty faculty) {
            this.id = faculty.id;
            this.name = faculty.name;
            this.groups = faculty.groups;
        }
        
        public Builder withId(String id) {
            this.id = id;
            return this;
        }
        
        public Builder withName(String name) {
            this.name = name;
            return this;
        }
        
        public Builder withGroups(List<Group> groups) {
            this.groups = groups;
            return this;
        }
        
        public Faculty build() {
            return new Faculty(this);
        }
    }
}
