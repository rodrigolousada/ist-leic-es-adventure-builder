package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelHasVacancyMethodTest {
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		new Room(this.hotel, "01", Type.DOUBLE);
	}

	@Test
	public void hasVacancy() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);

		Assert.assertEquals("01", room.getNumber());
	}

	@Test(expected = HotelException.class)
	public void nullRoomType() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		this.hotel.hasVacancy(null, arrival, departure);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		this.hotel.hasVacancy(Type.DOUBLE, null, departure);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		this.hotel.hasVacancy(Type.DOUBLE, arrival, null);
	}

	@Test(expected = HotelException.class)
	public void departureBeforeArrival() {
		LocalDate arrival = new LocalDate(2016, 12, 22);
		LocalDate departure = new LocalDate(2016, 12, 19);

		this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);
	}

	@Test
	public void hasNoVacancy1() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Hotel.reserveHotel(Type.DOUBLE, arrival, departure);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);
		Assert.assertNull(room);
	}

	@Test
	public void hasNoVacancy2() {
		LocalDate arrival1 = new LocalDate(2016, 12, 19);
		LocalDate departure1 = new LocalDate(2016, 12, 21);
		LocalDate arrival2 = new LocalDate(2016, 12, 18);
		LocalDate departure2 = new LocalDate(2016, 12, 20);

		Hotel.reserveHotel(Type.DOUBLE, arrival1, departure1);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival2, departure2);
		Assert.assertNull(room);
	}

	@Test
	public void hasNoVacancy3() {
		LocalDate arrival1 = new LocalDate(2016, 12, 19);
		LocalDate departure1 = new LocalDate(2016, 12, 21);
		LocalDate arrival2 = new LocalDate(2016, 12, 20);
		LocalDate departure2 = new LocalDate(2016, 12, 22);

		Hotel.reserveHotel(Type.DOUBLE, arrival1, departure1);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival2, departure2);
		Assert.assertNull(room);
	}

	@Test
	public void hasNoVacancy4() {
		LocalDate arrival1 = new LocalDate(2016, 12, 19);
		LocalDate departure1 = new LocalDate(2016, 12, 21);
		LocalDate arrival2 = new LocalDate(2016, 12, 19);
		LocalDate departure2 = new LocalDate(2016, 12, 20);

		Hotel.reserveHotel(Type.DOUBLE, arrival1, departure1);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival2, departure2);
		Assert.assertNull(room);
	}

	@Test
	public void hasNoVacancy5() {
		LocalDate arrival1 = new LocalDate(2016, 12, 19);
		LocalDate departure1 = new LocalDate(2016, 12, 21);
		LocalDate arrival2 = new LocalDate(2016, 12, 19);
		LocalDate departure2 = new LocalDate(2016, 12, 22);

		Hotel.reserveHotel(Type.DOUBLE, arrival1, departure1);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival2, departure2);
		Assert.assertNull(room);
	}

	@Test
	public void hasNoVacancy6() {
		LocalDate arrival1 = new LocalDate(2016, 12, 19);
		LocalDate departure1 = new LocalDate(2016, 12, 21);
		LocalDate arrival2 = new LocalDate(2016, 12, 18);
		LocalDate departure2 = new LocalDate(2016, 12, 21);

		Hotel.reserveHotel(Type.DOUBLE, arrival1, departure1);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival2, departure2);
		Assert.assertNull(room);
	}

	@Test
	public void hasNoVacancy7() {
		LocalDate arrival1 = new LocalDate(2016, 12, 19);
		LocalDate departure1 = new LocalDate(2016, 12, 21);
		LocalDate arrival2 = new LocalDate(2016, 12, 20);
		LocalDate departure2 = new LocalDate(2016, 12, 21);

		Hotel.reserveHotel(Type.DOUBLE, arrival1, departure1);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival2, departure2);
		Assert.assertNull(room);
	}

	@Test
	public void hasNoVacancy8() {
		LocalDate arrival1 = new LocalDate(2016, 12, 19);
		LocalDate departure1 = new LocalDate(2016, 12, 21);
		LocalDate arrival2 = new LocalDate(2016, 12, 18);
		LocalDate departure2 = new LocalDate(2016, 12, 22);

		Hotel.reserveHotel(Type.DOUBLE, arrival1, departure1);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival2, departure2);
		Assert.assertNull(room);
	}

	@Test
	public void hasNoVacancy9() {
		LocalDate arrival1 = new LocalDate(2016, 12, 19);
		LocalDate departure1 = new LocalDate(2016, 12, 22);
		LocalDate arrival2 = new LocalDate(2016, 12, 20);
		LocalDate departure2 = new LocalDate(2016, 12, 21);

		Hotel.reserveHotel(Type.DOUBLE, arrival1, departure1);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival2, departure2);
		Assert.assertNull(room);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
