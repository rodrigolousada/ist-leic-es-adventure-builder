package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelBulkBookingMethodTest extends RollbackTestAbstractClass {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel1, hotel2;

	@Override
	public void populate4Test() {
		this.hotel1 = new Hotel("XPTO123", "Paris");
		new Room(this.hotel1, "01", Type.DOUBLE);
		new Room(this.hotel1, "02", Type.SINGLE);
		new Room(this.hotel1, "03", Type.DOUBLE);
		new Room(this.hotel1, "04", Type.SINGLE);

		this.hotel2 = new Hotel("XPTO124", "Paris");
		new Room(this.hotel2, "01", Type.DOUBLE);
		new Room(this.hotel2, "02", Type.SINGLE);
		new Room(this.hotel2, "03", Type.DOUBLE);
		new Room(this.hotel2, "04", Type.SINGLE);
	}

	@Test
	public void success() {
		Set<String> references = Hotel.bulkBooking(2, this.arrival, this.departure);

		assertEquals(2, references.size());
	}

	@Test(expected = HotelException.class)
	public void zeroNumber() {
		Hotel.bulkBooking(0, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void noRooms() {
		hotel1.delete();
		hotel2.delete();
		this.hotel1 = new Hotel("XPTO124", "Paris");

		Hotel.bulkBooking(3, this.arrival, this.departure);
	}

	@Test
	public void OneNumber() {
		Set<String> references = Hotel.bulkBooking(1, this.arrival, this.departure);

		assertEquals(1, references.size());
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		Hotel.bulkBooking(2, null, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		Hotel.bulkBooking(2, this.arrival, null);
	}

	@Test
	public void reserveAll() {
		Set<String> references = Hotel.bulkBooking(8, this.arrival, this.departure);

		assertEquals(8, references.size());
	}

	@Test
	public void reserveAllPlusOne() {
		try {
			Hotel.bulkBooking(9, this.arrival, this.departure);
			fail();
		} catch (HotelException he) {
			assertEquals(8, Hotel.getAvailableRooms(8, this.arrival, this.departure).size());
		}
	}

}
