package com.trivago.mp.casestudy.dao;

import com.trivago.mp.casestudy.exception.DAOException;
import com.trivago.mp.casestudy.model.Advertiser;
import com.trivago.mp.casestudy.model.Hotel;

import java.util.List;

/**
 * DAO Interface for HotelSearch
 */
public interface HotelSearchDAO {
    /**
     * Fetch Hotel for a given hotelID.
     *
     * @param hotelID
     * @return {@link com.trivago.mp.casestudy.model.Hotel}
     * @throws DAOException
     */
    Hotel getHotelForID(String hotelID) throws DAOException;

    /**
     * Fetch Hotel List for a given cityName.
     *
     * @param city
     * @return List of Hotels ({@link com.trivago.mp.casestudy.model.Hotel})
     * @throws DAOException
     */
    List<Hotel> getHotelsForCity(String city) throws DAOException;

    /**
     * Fetch Advertiser List for a given hotelID.
     *
     * @param hotelID
     * @return List of Advertisers ({@link com.trivago.mp.casestudy.model.Advertiser})
     * @throws DAOException
     */
    List<Advertiser> getAdvertisersForHotel(int hotelID) throws DAOException;

    /**
     * Preload All Data.
     *
     * @throws DAOException
     */
    void preloadAllData() throws DAOException;
}
