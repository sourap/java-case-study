package com.trivago.mp.casestudy.model;

import com.trivago.mp.casestudy.service.OfferProvider;

import java.util.Objects;

/**
 * Class to Manage key for the Cache.
 */
public class CacheKey {
    private final String cityName;
    private final DateRange dateRange;
    private final OfferProvider offerProvider;

    public CacheKey(String cityName, DateRange dateRange, OfferProvider offerProvider) {
        this.cityName = cityName;
        this.dateRange = dateRange;
        this.offerProvider = offerProvider;
    }

    public OfferProvider getOfferProvider() {
        return offerProvider;
    }

    public String getCityName() {
        return cityName;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey cacheKey = (CacheKey) o;
        return Objects.equals(cityName, cacheKey.cityName) &&
                Objects.equals(dateRange, cacheKey.dateRange);
    }

    @Override
    public int hashCode() {

        return Objects.hash(cityName, dateRange);
    }

    @Override
    public String toString() {
        return "CacheKey{" +
                "cityName='" + cityName +
                ", dateRange=" + dateRange +
                '}';
    }
}