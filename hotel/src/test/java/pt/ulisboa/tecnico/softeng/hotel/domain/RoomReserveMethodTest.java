package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class RoomReserveMethodTest extends RollbackTestAbstractClass{
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private Room room;

	@Override
	public void populate4Test() {
		Hotel hotel = new Hotel("XPTO123", "Lisboa");
		this.room = new Room(hotel, "01", Type.SINGLE);
	}

	@Test
	public void success() {
		Booking booking = this.room.reserve(Type.SINGLE, this.arrival, this.departure);

		Assert.assertEquals(1, this.room.getNumberOfBookings());
		Assert.assertTrue(booking.getReference().length() > 0);
		Assert.assertEquals(this.arrival, booking.getArrival());
		Assert.assertEquals(this.departure, booking.getDeparture());
	}

	@Test(expected = HotelException.class)
	public void noDouble() {
		this.room.reserve(Type.DOUBLE, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullType() {
		this.room.reserve(null, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		this.room.reserve(Type.SINGLE, null, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		this.room.reserve(Type.SINGLE, this.arrival, null);
	}

	@Test
	public void allConflict() {
		this.room.reserve(Type.SINGLE, this.arrival, this.departure);

		try {
			this.room.reserve(Type.SINGLE, this.arrival, this.departure);
			fail();
		} catch (HotelException he) {
			Assert.assertEquals(1, this.room.getNumberOfBookings());
		}
	}
}
