package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BookingConstructorTest {

	@Test
	public void success() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		// define as instance variables to be reused in the different tests
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Booking booking = new Booking(hotel, arrival, departure);

		Assert.assertTrue(booking.getReference().startsWith(hotel.getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(arrival, booking.getArrival());
		Assert.assertEquals(departure, booking.getDeparture());
	}

	// did not test for null values

	@Test(expected = HotelException.class)
	public void date_failure() {
		Hotel hotel = new Hotel("XPTO123", "Londres");
		// better to test just in the day before
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 16);
		new Booking(hotel, arrival, departure);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
