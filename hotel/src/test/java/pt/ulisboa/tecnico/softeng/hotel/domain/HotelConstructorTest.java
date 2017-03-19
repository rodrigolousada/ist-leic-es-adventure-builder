package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelConstructorTest {

	@Before
	public void setUp() {

	}

	@Test
	public void success() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		Assert.assertEquals("Londres", hotel.getName());
		Assert.assertTrue(hotel.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(0, hotel.getNumberOfRooms());
		Assert.assertEquals(1, Hotel.hotels.size());
	}

	@Test
	public void nullCode() {
		try {
			new Hotel(null, "Londres");
			Assert.fail();
		} catch (HotelException he) {
			Assert.assertEquals(0, Hotel.hotels.size());
		}
	}

	@Test
	public void nullName() {
		try {
			new Hotel("XPT0123", null);
			Assert.fail();
		} catch (HotelException he) {
			Assert.assertEquals(0, Hotel.hotels.size());
		}
	}

	@Test
	public void emptyCode() {
		try {
			new Hotel("       ", "Londres");
			Assert.fail();
		} catch (HotelException he) {
			Assert.assertEquals(0, Hotel.hotels.size());
		}
	}

	@Test
	public void emptyName() {
		try {
			new Hotel("XPT0123", "    ");
			Assert.fail();
		} catch (HotelException he) {
			Assert.assertEquals(0, Hotel.hotels.size());
		}
	}

	@Test
	public void spacesCode() {
		try {
			new Hotel("  XP   ", "Londres");
			Assert.fail();
		} catch (HotelException he) {
			Assert.assertEquals(0, Hotel.hotels.size());
		}
	}

	@Test
	public void oversizedCode() {
		try {
			new Hotel("XPT01234", "Londres");
			Assert.fail();
		} catch (HotelException he) {
			Assert.assertEquals(0, Hotel.hotels.size());
		}
	}

	@Test
	public void undersizedCode() {
		try {
			new Hotel("XPT012", "Londres");
			Assert.fail();
		} catch (HotelException he) {
			Assert.assertEquals(0, Hotel.hotels.size());
		}
	}

	@Test
	public void uniqueCode() {
		Hotel hotel = new Hotel("XPTO123", "Londres");
		try {
			new Hotel("XPTO123", "Paris");
			Assert.fail();
		} catch (HotelException he) {
			Assert.assertEquals(1, Hotel.hotels.size());
			Assert.assertTrue(Hotel.hotels.contains(hotel));
		}
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
