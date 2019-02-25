package com.trivago.mp.casestudy.model;

import java.util.Objects;

public class City extends Identity {

    private final String name;

    public City(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + super.getId() +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return super.getId() == city.getId() &&
                Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.getId(), name);
    }
}
