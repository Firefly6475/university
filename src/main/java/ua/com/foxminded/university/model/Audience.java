package ua.com.foxminded.university.model;

import java.util.Objects;

public class Audience {
    private final String id;
    private final Integer number;
    private final Integer floor;
    
    public Audience(Builder builder) {
        this.id = builder.id;
        this.number = builder.number;
        this.floor = builder.floor;
    }

    public String getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getFloor() {
        return floor;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder (Audience audience) {
        return new Builder(audience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor, id, number);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Audience other = (Audience) obj;
        return Objects.equals(floor, other.floor)
                && Objects.equals(id, other.id)
                && Objects.equals(number, other.number);
    }

    @Override
    public String toString() {
        return "Audience [id=" + id + ", number=" + number + ", floor=" + floor + "]";
    }
    
    public static class Builder {
        private String id;
        private Integer number;
        private Integer floor;
        
        private Builder() {
            
        }
        
        private Builder (Audience audience) {
            this.id = audience.id;
            this.number = audience.number;
            this.floor = audience.floor;
        }
        
        public Builder withId(String id) {
            this.id = id;
            return this;
        }
        
        public Builder withNumber(Integer number) {
            this.number = number;
            return this;
        }
        
        public Builder withFloor(Integer floor) {
            this.floor = floor;
            return this;
        }
        
        public Audience build() {
            return new Audience(this);
        }
    }
}
