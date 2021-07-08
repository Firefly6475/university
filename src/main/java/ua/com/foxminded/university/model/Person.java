package ua.com.foxminded.university.model;

import java.util.Objects;

public abstract class Person {
    private String name;
    private String birthday;
    
    protected Person(String name, String birthday) {
        this.name = name;
        this.birthday = birthday;
    }
    
    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    @Override
    public int hashCode() {
        return Objects.hash(birthday, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Person other = (Person) obj;
        return Objects.equals(birthday, other.birthday) && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", birthday=" + birthday + "]";
    }
}
