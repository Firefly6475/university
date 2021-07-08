package ua.com.foxminded.university.model;

import java.util.Objects;

public class Audience {    
    private Integer number;
    private Integer floor;
    
    public Audience(Integer number, Integer floor) {
        this.number = number;
        this.floor = floor;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getFloor() {
        return floor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor, number);
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
        return Objects.equals(floor, other.floor) && Objects.equals(number, other.number);
    }

    @Override
    public String toString() {
        return "Audience [number=" + number + ", floor=" + floor + "]";
    }
}
