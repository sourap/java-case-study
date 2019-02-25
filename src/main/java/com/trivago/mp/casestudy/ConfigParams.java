package com.trivago.mp.casestudy;

/**
 * The config params controlled with runtime properties.
 */
public class ConfigParams {
    public static String DATA_DIRECTORY = System.getProperty("DATA_DIRECTORY")!=null ?
            System.getProperty("DATA_DIRECTORY") : "data";
    public static String HOTEL_FILENAME = System.getProperty("HOTEL_FILENAME")!=null ?
            System.getProperty("HOTEL_FILENAME") : "hotels.csv";
    public static String CITY_FILENAME = System.getProperty("CITY_FILENAME")!=null ?
            System.getProperty("CITY_FILENAME") : "cities.csv";
    public static String ADVERTISER_FILENAME = System.getProperty("ADVERTISER_FILENAME")!=null ?
            System.getProperty("ADVERTISER_FILENAME") : "advertisers.csv";
    public static String HOTEL_ADVERTISER_FILENAME = System.getProperty("HOTEL_ADVERTISER_FILENAME")!=null ?
            System.getProperty("HOTEL_ADVERTISER_FILENAME") : "hotel_advertiser.csv";
}
