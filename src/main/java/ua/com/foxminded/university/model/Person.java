package ua.com.foxminded.university.model;

import java.util.Objects;

public class Person {
    private final String name;
    private final String birthday;

    protected Person(Builder<?> builder) {
        this.name = builder.name;
        this.birthday = builder.birthday;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public static Builder builder() {
        return new Builder() {
            @Override
            public Builder getThis() {
                return this;
            }
        };
    }

    public static Builder builder (Person person) {
        return new Builder(person) {
            @Override
            public Builder getThis() {
                return this;
            }
        };
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

    public abstract static class Builder<T extends Builder<T>> {
        private String name;
        private String birthday;
        
        protected Builder() {
            
        }
        
        protected Builder(Person person) {
            this.name = person.name;
            this.birthday = person.birthday;
        }
        
        public abstract T getThis();

        public T withName(String name) {
            this.name = name;
            return this.getThis();
        }

        public T withBirthday(String birthday) {
            this.birthday = birthday;
            return this.getThis();
        }

        public Person build() {
            return new Person(this);
        }
    }
}
