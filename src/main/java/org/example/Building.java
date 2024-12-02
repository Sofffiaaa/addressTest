package org.example;

import java.util.Objects;

public class Building {
    public String city;
    public String street;
    public String house;
    public String floor;

    public Building(String city, String street, String house, String floor) {
        this.city = city;
        this.street = street;
        this.house = house;
        this.floor = floor;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Building build = (Building) obj;
        return city.equals(build.city) &&
                street.equals(build.street) &&
                house.equals(build.house) &&
                floor.equals(build.floor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, house, floor);
    }

    @Override
    public String toString() {
        return "Building{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", house='" + house + '\'' +
                ", floor='" + floor;
    }

}
