package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.fail;

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

	@Test
	public void success() {
		Room room = new Room(this.hotel, "01", Type.DOUBLE);

		Assert.assertEquals(this.hotel, room.getHotel());
		Assert.assertEquals("01", room.getNumber());
		Assert.assertEquals(Type.DOUBLE, room.getType());
		Assert.assertEquals(1, this.hotel.getNumberOfRooms());
	}

	@Test(expected = HotelException.class)
	public void nullHotel() {
		new Room(null, "01", Type.DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void nullRoomNumber() {
		new Room(this.hotel, null, Type.DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void emptyRoomNumber() {
		new Room(this.hotel, "", Type.DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void blankRoomNumber() {
		new Room(this.hotel, "     ", Type.DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void nonAlphanumericRoomNumber() {
		new Room(this.hotel, "JOSE", Type.DOUBLE);
	}

	@Test
	public void nonUniqueRoomNumber() {
		new Room(this.hotel, "01", Type.SINGLE);
		try {
			new Room(this.hotel, "01", Type.DOUBLE);
			fail();
		} catch (HotelException he) {
			Assert.assertEquals(1, this.hotel.getNumberOfRooms());
		}
	}

	@Test(expected = HotelException.class)
	public void nullType() {
		new Room(this.hotel, "01", null);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
