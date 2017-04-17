package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelCancelBookingMethodTest extends RollbackTestAbstractClass{
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	private Room room;
	private Booking booking;

	@Override
	public void populate4Test() {
		this.hotel = new Hotel("XPTO123", "Paris");
		this.room = new Room(this.hotel, "01", Type.DOUBLE);
		this.booking = this.room.reserve(Type.DOUBLE, this.arrival, this.departure);
	}

	@Test
	public void success() {
		String cancel = Hotel.cancelBooking(this.booking.getReference());

		Assert.assertTrue(this.booking.isCancelled());
		Assert.assertEquals(cancel, this.booking.getCancellation());
	}

	@Test(expected = HotelException.class)
	public void doesNotExist() {
		Hotel.cancelBooking("XPTO");
	}

	@Test(expected = HotelException.class)
	public void nullReference() {
		Hotel.cancelBooking(null);
	}

	@Test(expected = HotelException.class)
	public void emptyReference() {
		Hotel.cancelBooking("");
	}
}
