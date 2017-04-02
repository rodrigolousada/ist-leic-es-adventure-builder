package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@SuppressWarnings("deprecation")
public class HotelCancelBookingMethodTest {

	private final LocalDate arrival1 = new LocalDate(2016, 12, 19);
	private final LocalDate departure1 = new LocalDate(2016, 12, 24);
	private String reference1;
	private Room room1;

	@Before
	public void setUp() {
		Hotel hotel1 = new Hotel("XPTO123", "Londres");
		room1 = new Room(hotel1, "1", Room.Type.SINGLE);

		reference1 = Hotel.reserveRoom(Room.Type.SINGLE, arrival1, departure1);
	}

	@Test(expected = HotelException.class)
	public void emptyStringCancelBookingTest() {
		Hotel.cancelBooking("");
	}

	@Test(expected = HotelException.class)
	public void nonExistantHotelCancelBookingTest() {
		Hotel.cancelBooking("XPTO0124 asdg");
	}

	@Test(expected = HotelException.class)
	public void nonExistantBookingCancelBookingTest() {
		Hotel.cancelBooking("XPTO0123 asdg");
	}

	@Test
	public void cancellationDataCancelBookingTest() {
		reference1 = Hotel.cancelBooking(reference1);
		Assert.assertEquals(reference1, Hotel.getBooking(reference1).getCancellation());
		Assert.assertEquals(new LocalDate(), Hotel.getBooking(reference1).getCancellationDate());
	}

	@Test
	public void cancelledBookingRoomIsFreeTest() {
		Assert.assertTrue(!room1.isFree(Room.Type.SINGLE, arrival1, departure1));
		Hotel.cancelBooking(reference1);
		Assert.assertTrue(room1.isFree(Room.Type.SINGLE, arrival1, departure1));
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
