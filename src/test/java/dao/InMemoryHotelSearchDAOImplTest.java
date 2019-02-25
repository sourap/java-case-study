package dao;

import com.trivago.mp.casestudy.dao.HotelSearchDAO;
import com.trivago.mp.casestudy.dao.InMemoryHotelSearchDAOImpl;
import com.trivago.mp.casestudy.model.Advertiser;
import com.trivago.mp.casestudy.model.Hotel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class InMemoryHotelSearchDAOImplTest {

    private static HotelSearchDAO dao;

    @BeforeClass
    public static void init(){
        dao  = new InMemoryHotelSearchDAOImpl();
        dao.preloadAllData();
    }
    @Test
    public void getHotelForBadID() {
        final Hotel hotelForID = dao.getHotelForID("ABCD");
        Assert.assertNull(hotelForID);
    }

    @Test
    public void getHotelForGoodID() {
        final Hotel hotelForID = dao.getHotelForID("10");
        Assert.assertNotNull(hotelForID);
    }

    @Test
    public void getHotelsForBadCity() {
        final List<Hotel> hotelsForCity = dao.getHotelsForCity("ABCD");
        Assert.assertNotNull(hotelsForCity);
        Assert.assertEquals(0,hotelsForCity.size());
    }

    @Test
    public void getHotelsForCity() {
        final List<Hotel> hotelsForCity = dao.getHotelsForCity("Lissabon");
        Assert.assertNotNull(hotelsForCity);
        Assert.assertEquals(10,hotelsForCity.size());
    }

    @Test
    public void getAdvertiersForHotel() {
        final List<Advertiser> advertisers = dao.getAdvertisersForHotel(23);
        Assert.assertNotNull(advertisers);
        Assert.assertEquals(4,advertisers.size());

    }
}