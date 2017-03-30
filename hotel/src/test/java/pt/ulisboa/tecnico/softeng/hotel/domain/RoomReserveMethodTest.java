package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class RoomReserveMethodTest {
	Room room;

	@Before
	public void setUp() {
		Hotel hotel = new Hotel("XPTO123", "Lisboa");
		this.room = new Room(hotel, "01", Type.SINGLE);
	}

	// did not test for nulls

	@Test
	public void success() {
		// define as instance variables to be reused
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 24);
		Booking booking = this.room.reserve(Type.SINGLE, arrival, departure);

		Assert.assertTrue(booking.getReference().length() > 0);
		Assert.assertEquals(arrival, booking.getArrival());
		Assert.assertEquals(departure, booking.getDeparture());
	}

	@Test(expected = HotelException.class)
	public void unavailableRoomReserve() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 24);
		// not necessary to declare the variables
		Booking booking = this.room.reserve(Type.SINGLE, arrival, departure);
		Booking booking2 = this.room.reserve(Type.SINGLE, arrival, departure);
	}

	@Test(expected = HotelException.class)
	public void conflictDepartureBeforeArrival() {
		LocalDate arrival = new LocalDate(2016, 12, 10);
		LocalDate departure = new LocalDate(2016, 12, 8);

		Booking booking = this.room.reserve(Type.SINGLE, arrival, departure);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
