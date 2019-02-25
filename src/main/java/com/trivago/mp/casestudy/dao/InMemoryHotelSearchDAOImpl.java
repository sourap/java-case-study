package com.trivago.mp.casestudy.dao;

import com.trivago.mp.casestudy.ConfigParams;
import com.trivago.mp.casestudy.exception.DAOException;
import com.trivago.mp.casestudy.model.Advertiser;
import com.trivago.mp.casestudy.model.City;
import com.trivago.mp.casestudy.model.Hotel;
import com.trivago.mp.casestudy.model.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.trivago.mp.casestudy.ConfigParams.DATA_DIRECTORY;

/**
 * InMemory implementation of the DAO
 */
public class InMemoryHotelSearchDAOImpl implements HotelSearchDAO {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryHotelSearchDAOImpl.class);
    private Map<String, Hotel> hotelMap;
    private Map<String, City> cityMap;
    private Map<String, Advertiser> advertiserMap;
    private Map<String, List<Advertiser>> hotelAdvertiserMap;
    private Map<String, List<Hotel>> hotelCityMap;

    private Function<String, Hotel> mapHotels = (line) -> {
        String[] values = line.split(",");
        int cityID;
        try {
            Hotel hotel = null;
            hotel = new Hotel(Integer.parseInt(values[0]), values[4], Integer.parseInt(values[5]), Integer.parseInt(values[6]),
                    new City(cityID = Integer.parseInt(values[1]), this.cityMap.get(cityID).getName()));
            return hotel;
        } catch (NumberFormatException e) {
            logger.error("Error in reading line " + line);
            return null;
        }
    };
    private Function<String, Advertiser> mapAdvertises = (line) -> {
        String[] values = line.split(",");
        try {
            Advertiser advertiser = new Advertiser(Integer.parseInt(values[0]), values[1]);
            return advertiser;
        } catch (NumberFormatException e) {
            logger.error("Error in reading line " + line);
            return null;
        }
    };
    private Function<String, City> mapCities = (line) -> {
        String[] values = line.split(",");
        try {
            City city = new City(Integer.parseInt(values[0]), values[1]);
            return city;
        } catch (NumberFormatException e) {
            logger.error("Error in reading line " + line);
            return null;
        }
    };

    @Override
    public Hotel getHotelForID(String hotelID) {
        logger.info("Get Hotel For ID : "+hotelID);
        return hotelMap.get(hotelID);
    }

    @Override
    public List<Hotel> getHotelsForCity(String city) {
        logger.info("getHotelsForCity invoked for " + city);
        return hotelCityMap.getOrDefault(city, Collections.emptyList());
    }

    @Override
    public void preloadAllData() {
        logger.info("Preloading Data");
        cityMap = loadAllCities(ConfigParams.CITY_FILENAME);
        hotelMap = loadAllHotelsMap(ConfigParams.HOTEL_FILENAME);
        advertiserMap = loadAllAdvertisers(ConfigParams.ADVERTISER_FILENAME);
        hotelAdvertiserMap = getAllAdvertisersByHotel(ConfigParams.HOTEL_ADVERTISER_FILENAME);
        hotelCityMap = loadAllHotelsByCityName(ConfigParams.HOTEL_FILENAME);
        logger.info("Preloading Data Complete");
    }

    @Override
    public List<Advertiser> getAdvertisersForHotel(int hotelID) {
        logger.info("getAdvertisersForHotel invoked for " + hotelID);
        return hotelAdvertiserMap.get(Integer.toString(hotelID));
    }

    private Map<String, City> loadAllCities(String fileName) {
        logger.info("Loading Cities from File: "+fileName);
        return processForMap(fileName, mapCities);
    }

    private Map<String, Advertiser> loadAllAdvertisers(String fileName) {
        logger.info("Loading Advertisers from File: "+fileName);
        return processForMap(fileName, mapAdvertises);
    }

    private Map<String, List<Advertiser>> getAllAdvertisersByHotel(String fileName) {
        logger.info("Loading Advertisers By Hotel from File: "+fileName);
        return processForMapWithIndexes(fileName, 1, 0);
    }

    private Map<String, List<String>> getAllHotelsByAdvertiser(String fileName) {
        logger.info("Loading Hotels By Advertisers from File: "+fileName);
        return processForMapWithID(fileName, 0, 1);
    }

    private Map<String, List<Hotel>> loadAllHotelsByCityName(String fileName) {
        return loadAllHotels(fileName).stream().
                collect(Collectors.groupingBy(h -> h.getCity().getName()));

    }

    private List<Hotel> loadAllHotels(String fileName) {
        logger.info("Loading HOTELS from File: "+fileName);
        Path path = Paths.get(DATA_DIRECTORY, fileName);
        try (Stream<String> contentStream = Files.lines(path)) {
            return contentStream.skip(1).map(mapHotels).filter(h -> Objects.nonNull(h)).
                    collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("IOException encountered : ", e);
            throw new DAOException(e);
        }
    }

    private Map<String, Hotel> loadAllHotelsMap(String fileName) {
        return loadAllHotels(fileName).stream().collect(Collectors.toMap(hotel -> Integer.toString(hotel.getId()),
                Function.identity()));
    }

    private Map processForMap(String fileName, Function<String, ? extends Identity> function) {
        Path path = Paths.get(DATA_DIRECTORY, fileName);
        try (Stream<String> contentStream = Files.lines(path)) {
            return contentStream.skip(1).map(function).collect(Collectors.toMap(e -> e.getId(), Function.identity()));
        } catch (IOException e) {
            logger.error("IOException encountered : ", e);
            throw new DAOException(e);
        }
    }

    private Map<String, List<String>> processForMapWithID(String fileName, int idIndex, int valIndex) {
        Path path = Paths.get(DATA_DIRECTORY, fileName);
        try (Stream<String> contentStream = Files.lines(path)) {
            return contentStream.skip(1).map(line -> line.split(",")).collect(Collectors.groupingBy(e -> e[idIndex],
                    Collectors.mapping(e -> e[valIndex], Collectors.toList())));
        } catch (IOException e) {
            logger.error("IOException encountered : ", e);
            throw new DAOException(e);
        }
    }

    private Map<String, List<Advertiser>> processForMapWithIndexes(String fileName, int idIndex, int valIndex) {
        Path path = Paths.get(DATA_DIRECTORY, fileName);
        try (Stream<String> contentStream = Files.lines(path)) {
            return contentStream.skip(1)
                    .map(line -> line.split(","))
                    .collect(Collectors.groupingBy(e -> e[idIndex], Collectors.mapping(e -> advertiserMap.get(Integer.parseInt(e[valIndex])),
                            Collectors.toList())));
        } catch (IOException e) {
            logger.error("IOException encountered : ", e);
            throw new DAOException(e);
        }
    }
}
