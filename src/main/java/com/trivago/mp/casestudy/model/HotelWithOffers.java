package com.trivago.mp.casestudy.model;

import java.util.List;
import java.util.Objects;

/**
 * Wraps a hotel and a list of {@link Offer Offers}.
 */
public class HotelWithOffers {
    private final Hotel hotel;

    /**
     * A list of concrete advertiser offers for this hotel
     */
    List<Offer> offers;

    public HotelWithOffers(Hotel hotel) {
        this.hotel = hotel;
    }

    public HotelWithOffers(Hotel hotel, List<Offer> offers) {
        this.hotel = hotel;
        this.offers = offers;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    @Override
    public String toString() {
        return "HotelWithOffers{" + "hotel=" + hotel + ", offers=" + offers + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelWithOffers that = (HotelWithOffers) o;
        return Objects.equals(hotel, that.hotel) &&
                Objects.equals(offers, that.offers);
    }

    @Override
    public int hashCode() {

        return Objects.hash(hotel, offers);
    }
}
