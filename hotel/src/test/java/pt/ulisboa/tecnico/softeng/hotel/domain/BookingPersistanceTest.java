package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class BookingPersistanceTest {
	private static final String HOTEL_CODE = "HOTEL01";
	private static final String HOTEL_NAME = "Ritz";
	private static final String ROOM_NUMBER = "010";
	private static final Room.Type ROOM_TYPE = Room.Type.SINGLE;

	private final LocalDate start = new LocalDate(2017, 04, 20);
	private final LocalDate end = new LocalDate(2017, 04, 25);

	private Hotel local_hotel;
	private Room local_room;
	private String booking_reference;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		this.local_hotel = new Hotel(HOTEL_CODE, HOTEL_NAME);
		this.local_room = new Room(local_hotel, ROOM_NUMBER, ROOM_TYPE);
		this.booking_reference = Hotel.reserveRoom(ROOM_TYPE, start, end);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		assertEquals(1, FenixFramework.getDomainRoot().getHotelSet().size());

		List<Hotel> hotels = new ArrayList<>(FenixFramework.getDomainRoot().getHotelSet());

		Hotel hotel = null;

		for (Hotel h : hotels) {
			if (h.getCode() == HOTEL_CODE)
				hotel = h;
		}
		assertNotNull(hotel);

		Room room = null;
		List<Room> rooms = new ArrayList<>(hotel.getRoomSet());
		for (Room r : rooms) {
			if (r.getNumber() == ROOM_NUMBER) {
				room = r;
			}
		}
		Booking booking = room.getBooking(booking_reference);
		assertEquals(start, booking.getArrival());
		assertEquals(end, booking.getDeparture());
		assertEquals(room, booking.getRoom());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		local_hotel.delete();
	}

}
