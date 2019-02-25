package com.trivago.mp.casestudy.service.impl;


import com.trivago.mp.casestudy.dao.HotelSearchDAO;
import com.trivago.mp.casestudy.dao.InMemoryHotelSearchDAOImpl;
import com.trivago.mp.casestudy.exception.DAOException;
import com.trivago.mp.casestudy.exception.ServiceException;
import com.trivago.mp.casestudy.model.*;
import com.trivago.mp.casestudy.service.HotelSearchEngine;
import com.trivago.mp.casestudy.service.OfferProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Your task will be to implement two functions, one for loading the data which is stored as .csv files in the ./data
 * folder and one for performing the actual search.
 */
public class HotelSearchEngineImpl implements HotelSearchEngine {

    private static final Logger logger = LoggerFactory.getLogger(HotelSearchEngineImpl.class);
    private final HotelSearchDAO hotelSearchDAO;
    private final ConcurrentMap<CacheKey, List<HotelWithOffers>> cache;

    public HotelSearchEngineImpl(HotelSearchDAO hotelSearchDAO) {
        this.hotelSearchDAO = hotelSearchDAO;
        cache = new ConcurrentHashMap<>();
    }

    public HotelSearchEngineImpl() {
        this.hotelSearchDAO = new InMemoryHotelSearchDAOImpl();
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public void initialize() {
        logger.info("Initialize HotelSearchEngineImpl");
        try {
            hotelSearchDAO.preloadAllData();
        } catch (DAOException e) {
            logger.error("DAO Exception encountered ", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<HotelWithOffers> performSearch(String cityName, DateRange dateRange, OfferProvider offerProvider) {
        logger.info("performSearch HotelSearchEngineImpl");
        validateInputs(cityName, dateRange, offerProvider); //Validate the inputs
        CacheKey key = new CacheKey(cityName, dateRange, offerProvider); // Create Cache Key
        logger.info("performSearch HotelSearchEngineImpl Input : " + key);
        return cache.computeIfAbsent(key, preformActualSearch());
    }

    private Function<CacheKey, List<HotelWithOffers>> preformActualSearch() {
        return key -> {
            try {
                List<HotelWithOffers> hotelWithOffersList = hotelSearchDAO.getHotelsForCity(key.getCityName()) // Get Hotels for a given city
                        .stream()
                        .map(hotelID -> hotelID.getId()) // Get Hotel IDs for a given city
                        .collect(Collectors.toList()) // List of Hotel IDs for a given city
                        .stream().map(hotelID -> getOffers(key, hotelID)) // Get Offer for a given hotel ID
                        .collect(Collectors.toList());
                return hotelWithOffersList;
            } catch (DAOException e) {
                logger.error("DAO Exception encountered ", e);
                throw new ServiceException(e);
            }
        };
    }

    private HotelWithOffers getOffers(CacheKey key, Integer hotelID) {
        final List<Advertiser> advertisersForHotel = hotelSearchDAO.getAdvertisersForHotel(hotelID); // Get Advertisers for a given HotelID
        List<Offer> offerList = advertisersForHotel.stream()
                .map(advertiser -> key.getOfferProvider()
                        .getOffersFromAdvertiser(advertiser, Arrays.asList(hotelID), key.getDateRange()) // Expensive Network call
                        .entrySet()).flatMap(entry -> entry.stream()).map(e -> e.getValue())
                .collect(Collectors.toList()); // OfferList for a given HotelID
        return new HotelWithOffers(hotelSearchDAO.getHotelForID(Integer.toString(hotelID)), offerList); // Construct HotelWithOffers Object
    }

    private void validateInputs(String cityName, DateRange dateRange, OfferProvider offerProvider) {
        if (!(Objects.nonNull(cityName) && cityName.trim().length() > 0)) {
            String msg = "CityName expected";
            logger.error(msg);
            throw new ServiceException("Input Validation Exception : " + msg);
        }
        if (!(Objects.nonNull(dateRange) && dateRange.getStartDate() > 0)) {
            String msg = "DateRange.StartDate expected";
            logger.error(msg);
            throw new ServiceException("Input Validation Exception : " + msg);
        }
        if (!(Objects.nonNull(dateRange) && dateRange.getEndDate() > 0)) {
            String msg = "DateRange.EndDate expected";
            logger.error(msg);
            throw new ServiceException("Input Validation Exception : " + msg);
        }
        if (dateRange.getStartDate() > dateRange.getEndDate()) {
            String msg = "start date must be before End date";
            logger.error(msg);
            throw new ServiceException("Input Validation Exception : " + msg);
        }
        if (Objects.isNull(offerProvider)) {
            String msg = "OfferProvider expected";
            logger.error(msg);
            throw new ServiceException("Input Validation Exception : " + msg);
        }
    }
}
