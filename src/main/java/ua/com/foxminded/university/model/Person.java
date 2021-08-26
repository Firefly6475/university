package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Person {
    private final String id;
    private final String name;
    private final LocalDate birthday;

    protected Person(Builder<?> builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.birthday = builder.birthday;
    }

    public static Builder builder() {
        return new Builder() {
            @Override
            public Builder getThis() {
                return this;
            }
        };
    }

    public static Builder builder(Person person) {
        return new Builder(person) {
            @Override
            public Builder getThis() {
                return this;
            }
        };
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public int hashCode() {
        return Objects.hash(birthday, id, name);
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
        return Objects.equals(birthday, other.birthday)
                && Objects.equals(id, other.id)
                && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    public abstract static class Builder<T extends Builder<T>> {
        private String id;
        private String name;
        private LocalDate birthday;

        protected Builder() {

        }

        protected Builder(Person person) {
            this.id = person.id;
            this.name = person.name;
            this.birthday = person.birthday;
        }

        public abstract T getThis();

        public T withId(String id) {
            this.id = id;
            return this.getThis();
        }

        public T withName(String name) {
            this.name = name;
            return this.getThis();
        }

        public T withBirthday(LocalDate birthday) {
            this.birthday = birthday;
            return this.getThis();
        }

        public Person build() {
            return new Person(this);
        }
    }
}
