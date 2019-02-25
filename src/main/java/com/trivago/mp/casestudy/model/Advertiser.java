package com.trivago.mp.casestudy.model;

import java.util.Objects;

/**
 * Stores the id and name of an advertiser. An Advertiser is a company provides offers for hotel stays.
 */
public class Advertiser extends Identity {
    private final String name;

    public Advertiser(int id, String name) {
        super(id);
        this.name = name;
    }

    /**
     * A unique id as specified in the corresponding .csv file
     *
     * @return
     */

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Advertiser{" + "id=" + super.getId() + ", name='" + name + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advertiser that = (Advertiser) o;
        return super.getId() == that.getId() &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.getId(), name);
    }
}
