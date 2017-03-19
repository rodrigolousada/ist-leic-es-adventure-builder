package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelConstructorTest {
	private static final String HOTEL_NAME = "Londres";
	private static final String HOTEL_CODE = "XPTO123";

	@Test
	public void success() {
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME);

		Assert.assertEquals(HOTEL_NAME, hotel.getName());
		Assert.assertTrue(hotel.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(0, hotel.getNumberOfRooms());
		Assert.assertEquals(1, Hotel.hotels.size());
	}

	@Test(expected = HotelException.class)
	public void nullCode() {
		new Hotel(null, HOTEL_NAME);
	}

	@Test(expected = HotelException.class)
	public void blankCode() {
		new Hotel("      ", HOTEL_NAME);
	}

	@Test(expected = HotelException.class)
	public void emptyCode() {
		new Hotel("", HOTEL_NAME);
	}

	@Test(expected = HotelException.class)
	public void nullName() {
		new Hotel(HOTEL_CODE, null);
	}

	@Test(expected = HotelException.class)
	public void blankName() {
		new Hotel(HOTEL_CODE, "  ");
	}

	@Test(expected = HotelException.class)
	public void emptyName() {
		new Hotel(HOTEL_CODE, "");
	}

	@Test(expected = HotelException.class)
	public void codeSizeLess() {
		new Hotel("123456", HOTEL_NAME);
	}

	@Test(expected = HotelException.class)
	public void codeSizeMore() {
		new Hotel("12345678", HOTEL_NAME);
	}

	@Test(expected = HotelException.class)
	public void codeNotUnique() {
		new Hotel(HOTEL_CODE, HOTEL_NAME);
		new Hotel(HOTEL_CODE, HOTEL_NAME + " City");
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
