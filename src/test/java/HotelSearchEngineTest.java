import com.trivago.mp.casestudy.dao.HotelSearchDAO;
import com.trivago.mp.casestudy.dao.InMemoryHotelSearchDAOImpl;
import com.trivago.mp.casestudy.exception.ServiceException;
import com.trivago.mp.casestudy.model.Advertiser;
import com.trivago.mp.casestudy.model.DateRange;
import com.trivago.mp.casestudy.model.HotelWithOffers;
import com.trivago.mp.casestudy.model.Offer;
import com.trivago.mp.casestudy.service.HotelSearchEngine;
import com.trivago.mp.casestudy.service.OfferProvider;
import com.trivago.mp.casestudy.service.impl.HotelSearchEngineImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;

public class HotelSearchEngineTest {
    private static HotelSearchEngine hotelSearchEngine;
    private final Random random = new Random(42);
    private OfferProvider dummyOfferProvider = (advertiser, hotelIds, dateRange) -> {
        Map<Integer, Offer> result = new HashMap<>();
        for (Integer hotelId : hotelIds) {
            if (random.nextDouble() > 0.25) {
                result.put(hotelId, new Offer(advertiser, random.nextInt(100) + 50, random.nextInt(10)));
            }
        }
        return result;
    };

    @BeforeClass
    public static void setup() {
        hotelSearchEngine = new HotelSearchEngineImpl();
        hotelSearchEngine.initialize();
    }

    @Test
    public void testNoException() {
        try {
            hotelSearchEngine.performSearch("Berlin", new DateRange(20180214, 201802016), dummyOfferProvider);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testResultsReturned() {
        List<HotelWithOffers> hotelsWithOffers = hotelSearchEngine.performSearch("Berlin",
                new DateRange(20180214, 201802016),
                dummyOfferProvider
        );
        assertFalse("the result is not empty", hotelsWithOffers.isEmpty());
    }

    @Test
    public void testCorrectHotelsReturned() {
        List<HotelWithOffers> hotelsWithOffers = hotelSearchEngine.performSearch("Munich",
                new DateRange(20180214, 201802016),
                dummyOfferProvider
        );

        @SuppressWarnings("serial") Set<Integer> hotelsInMunich = new HashSet<Integer>() {{
            addAll(Arrays.asList(89,
                    90,
                    91,
                    92,
                    93,
                    94,
                    95,
                    96,
                    97,
                    98,
                    99,
                    100,
                    101,
                    102,
                    103,
                    104,
                    105,
                    106,
                    107,
                    108,
                    109,
                    110,
                    111,
                    112,
                    113,
                    114,
                    115,
                    116,
                    117,
                    118,
                    119,
                    120,
                    121,
                    122,
                    123,
                    124,
                    125,
                    126,
                    127,
                    128,
                    129,
                    130,
                    131,
                    132,
                    133,
                    134,
                    135,
                    136,
                    137,
                    138,
                    139,
                    140,
                    141,
                    142,
                    143,
                    144,
                    145,
                    146,
                    147,
                    148,
                    149,
                    150,
                    151,
                    152,
                    153,
                    154,
                    155,
                    156,
                    157,
                    158,
                    159,
                    160,
                    161,
                    162,
                    163,
                    164,
                    165,
                    166,
                    167,
                    168,
                    169,
                    170,
                    171,
                    172,
                    173,
                    174,
                    175
            ));
        }};

        for (HotelWithOffers hotelWithOffers : hotelsWithOffers) {
            assertTrue(String.format(
                    "returned wrong hotel, hotel %d is not in Munich",
                    hotelWithOffers.getHotel().getId()
            ), hotelsInMunich.contains(hotelWithOffers.getHotel().getId()));
        }
    }

    @Test(expected = ServiceException.class)
    public void testWrongDates() {
        hotelSearchEngine.performSearch("Berlin", new DateRange(201802016, 20180224), dummyOfferProvider);
    }

    @Test
    public void testCacheDataTime() {
        long start, end, beforeCache, afterCache;
        start = System.nanoTime();
        hotelSearchEngine.performSearch("Berlin", new DateRange(20180224, 20180225), dummyOfferProvider);
        end = System.nanoTime();
        beforeCache = end - start;
        start = System.nanoTime();
        hotelSearchEngine.performSearch("Berlin", new DateRange(20180224, 20180225), dummyOfferProvider);
        end = System.nanoTime();
        afterCache = end - start;
        assertTrue((afterCache) < beforeCache);
    }

    @Test
    public void testCacheDataIncorrectCitySearch() {
        List<HotelWithOffers> hotelsWithOffers;
        hotelsWithOffers = hotelSearchEngine.performSearch("ABCD", new DateRange(20180224, 20180225), dummyOfferProvider);
        Assert.assertNotNull(hotelsWithOffers);
        Assert.assertEquals(0, hotelsWithOffers.size());
    }

    @Test
    public void testCacheDataLisabon() {
        HotelSearchDAO dao = new InMemoryHotelSearchDAOImpl();
        HotelSearchEngine hotelSearchEngine = new HotelSearchEngineImpl(dao);
        List<HotelWithOffers> hotelsWithOffers;
        hotelSearchEngine.initialize();
        hotelsWithOffers = hotelSearchEngine.performSearch("Lissabon", new DateRange(20180224, 20180225), dummyOfferProvider);
        Assert.assertEquals(10, dao.getHotelsForCity("Lissabon").size());
        final List<Advertiser> advertiserList = dao.getHotelsForCity("Lissabon").stream().map(hotel -> dao
                .getAdvertisersForHotel(hotel.getId())).flatMap(hotel -> hotel.stream()).collect(Collectors.toList());
        Assert.assertEquals(48, advertiserList.size());
        Assert.assertNotNull(hotelsWithOffers);
        Assert.assertEquals(10, hotelsWithOffers.size());
        Assert.assertEquals(42, hotelsWithOffers.stream().map(h -> h.getOffers()).flatMap(h -> h.stream()).count());
    }

    @Test(expected = ServiceException.class)
    public void testInvalidInputsCity() {
        hotelSearchEngine.performSearch(null, new DateRange(20180224, 20180225), dummyOfferProvider);
    }

    @Test(expected = ServiceException.class)
    public void testInvalidStartDate() {
        hotelSearchEngine.performSearch("Berlin", new DateRange(0, 20180224), dummyOfferProvider);
    }

    @Test(expected = ServiceException.class)
    public void testInvalidEndDate() {
        hotelSearchEngine.performSearch("Berlin", new DateRange(10, 0), dummyOfferProvider);
    }

    @Test(expected = ServiceException.class)
    public void testInvalidInputsDateRange() {
        hotelSearchEngine.performSearch("Berlin", new DateRange(20180225, 20180224), dummyOfferProvider);
    }

    @Test(expected = ServiceException.class)
    public void testInvalidOfferProvider() {
        hotelSearchEngine.performSearch("Berlin", new DateRange(20180223, 20180224), null);
    }
}
