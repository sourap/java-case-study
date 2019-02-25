package com.trivago.mp.casestudy.model;

import java.util.Objects;

/**
 * Stores all relevant information about a particular hotel.
 */
public class Hotel extends Identity {
    // private final int id;
    private final String name;
    private final int rating;
    private final int stars;
    private final City city;


    public Hotel(int id, String name, int rating, int stars, City city) {
        super(id);// this.id = id;
        this.name = name;
        this.rating = rating;
        this.stars = stars;
        this.city = city;
    }

    /**
     * A unique id as specified in the corresponding .csv file
     *
     * @return
     */
    // public int getId() {
    //    return id;
    //}

    /**
     * The English name of the hotel
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * The ratings given by user feedback, between 0-100
     *
     * @return
     */
    public int getRating() {
        return rating;
    }

    /**
     * The star rating for the given hotel, between 1-5
     *
     * @return
     */
    public int getStars() {
        return stars;
    }

    public City getCity() {
        return city;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return super.getId() == hotel.getId() &&
                rating == hotel.rating &&
                stars == hotel.stars &&
                Objects.equals(name, hotel.name) &&
                Objects.equals(city, hotel.city);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.getId(), name, rating, stars, city);
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id='" + super.getId() + '\'' +
                "name='" + name + '\'' +
                ", rating=" + rating +
                ", stars=" + stars +
                ", city=" + city +
                '}';
    }
}
