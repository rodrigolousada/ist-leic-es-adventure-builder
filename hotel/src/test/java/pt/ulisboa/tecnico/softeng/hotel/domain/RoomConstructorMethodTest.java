package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class RoomConstructorMethodTest {
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Lisboa");
	}

	// did not test for nulls in hotel and room type

	@Test
	public void success() {
		Room room = new Room(this.hotel, "01", Type.DOUBLE);

		Assert.assertEquals(this.hotel, room.getHotel());
		Assert.assertEquals("01", room.getNumber());
		Assert.assertEquals(Type.DOUBLE, room.getType());
		Assert.assertEquals(1, this.hotel.getNumberOfRooms());
	}

	@Test
	public void numerical_failure() {
		try {
			new Room(this.hotel, "1A", Type.DOUBLE);
			Assert.fail();
		} catch (HotelException e) {
			Assert.assertEquals(0, this.hotel.getNumberOfRooms());
		}
	}

	@Test
	public void numerical_failure_empty() {
		try {
			new Room(this.hotel, "", Type.DOUBLE);
			Assert.fail();
		} catch (HotelException e) {
			Assert.assertEquals(0, this.hotel.getNumberOfRooms());
		}
	}

	@Test
	public void numerical_failure_null() {
		try {
			new Room(this.hotel, null, Type.DOUBLE);
			Assert.fail();
		} catch (HotelException e) {
			Assert.assertEquals(0, this.hotel.getNumberOfRooms());
		}
	}

	@Test
	public void unique_failure() {
		Room room = new Room(this.hotel, "10", Type.DOUBLE);

		try {
			new Room(this.hotel, "10", Type.DOUBLE);
			Assert.fail();
		} catch (HotelException e) {
			Assert.assertEquals(1, this.hotel.getNumberOfRooms());
			Assert.assertTrue(this.hotel.getRooms().contains(room));
		}
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
