package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertNull;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelBulkBookingMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	private Room room;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		this.room = new Room(this.hotel, "01", Type.DOUBLE);
		this.room = new Room(this.hotel, "02", Type.DOUBLE);
		this.room = new Room(this.hotel, "03", Type.SINGLE);
		this.room = new Room(this.hotel, "04", Type.SINGLE);
		this.room = new Room(this.hotel, "05", Type.DOUBLE);
		this.room = new Room(this.hotel, "06", Type.DOUBLE);
	}

	@Test
	public void bulkBookingSuccess() {
		Set<String> bookings = Hotel.bulkBooking(6, this.arrival, this.departure);	
		Assert.assertEquals(6, bookings.size());
	}
	
	@Test (expected = HotelException.class)
	public void bulkBookingZeroNumber() {
		Hotel.bulkBooking(0, this.arrival, this.departure);	
	}
	
	@Test (expected = HotelException.class)
	public void bulkBookingNegativeNumber() {
		Hotel.bulkBooking(-5, this.arrival, this.departure);	
	}
	
	@Test (expected = HotelException.class)
	public void bulkBookingNullArrival() {
		Hotel.bulkBooking(1, null, this.departure);	
	}
	
	@Test (expected = HotelException.class)
	public void bulkBookingNullDeparture() {
		Hotel.bulkBooking(1, this.arrival, null);	
	}
	
	@Test (expected = HotelException.class)
	public void bulkBookingOversizedNumber() {
		Hotel.bulkBooking(7, this.arrival, this.departure);	
	}
	
	@Test (expected = HotelException.class)
	public void bulkBookingDepartureBeforeArrival() {
		Hotel.bulkBooking(5, this.departure, this.arrival);	
	}
	
	@Test (expected = HotelException.class)
	public void bulkBookingAfterReservation() {
		Hotel.reserveRoom(Type.SINGLE, arrival, departure);
		Hotel.bulkBooking(6, this.arrival, this.departure);
	}
	
	@Test (expected = HotelException.class)
	public void bulkBookingBeforeReservation() {
		Hotel.bulkBooking(6, this.arrival, this.departure);
		Hotel.reserveRoom(Type.SINGLE, arrival, departure);
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
