package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelHasVacancyMethodTest extends RollbackTestAbstractClass {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	private Room room;

	@Override
	public void populate4Test() {
		this.hotel = new Hotel("XPTO123", "Paris");
		this.room = new Room(this.hotel, "01", Type.DOUBLE);
	}

	@Test
	public void hasVacancy() {
		Room room = this.hotel.hasVacancy(Type.DOUBLE, this.arrival, this.departure);

		Assert.assertNotNull(room);
		Assert.assertEquals("01", room.getNumber());
	}

	@Test
	public void noVacancy() {
		this.room.reserve(Type.DOUBLE, this.arrival, this.departure);

		assertNull(this.hotel.hasVacancy(Type.DOUBLE, this.arrival, this.departure));
	}

	@Test
	public void noVacancyEmptyRoomSet() {
		Hotel otherHotel = new Hotel("XPTO124", "Paris Germain");

		assertNull(otherHotel.hasVacancy(Type.DOUBLE, this.arrival, this.departure));
	}

	@Test(expected = HotelException.class)
	public void nullType() {
		this.hotel.hasVacancy(null, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		this.hotel.hasVacancy(Type.DOUBLE, null, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		this.hotel.hasVacancy(Type.DOUBLE, this.arrival, null);
	}

}
