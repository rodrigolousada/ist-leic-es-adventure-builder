package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class RoomGetBookingMethodTest extends RollbackTestAbstractClass{ 
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private Hotel hotel;
	private Room room;
	private Booking booking;

	@Override
	public void populate4Test() {
		this.hotel = new Hotel("XPTO123", "Lisboa");
		this.room = new Room(this.hotel, "01", Type.SINGLE);
		this.booking = this.room.reserve(Type.SINGLE, this.arrival, this.departure);
	}

	@Test
	public void success() {
		assertEquals(this.booking, this.room.getBooking(this.booking.getReference()));
	}

	@Test
	public void successCancelled() {
		this.booking.cancel();

		assertEquals(this.booking, this.room.getBooking(this.booking.getCancellation()));
	}

	@Test
	public void doesNotExist() {
		assertNull(this.room.getBooking("XPTO"));
	}
}
