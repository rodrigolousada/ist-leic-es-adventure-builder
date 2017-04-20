package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class RoomPersistanceTest {
	private static final String HOTEL_CODE = "HOTEL01";
	private static final String HOTEL_NAME = "Ritz";
	private static final String ROOM_NUMBER = "010";
	private static final Room.Type ROOM_TYPE = Room.Type.SINGLE;

	private Hotel local_hotel;
	private Room local_room;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		this.local_hotel = new Hotel(HOTEL_CODE, HOTEL_NAME);

		local_room = new Room(local_hotel, ROOM_NUMBER, ROOM_TYPE);
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

		List<Room> rooms = new ArrayList<>(hotel.getRoomSet());
		Room room = rooms.get(0);

		assertEquals(local_room, room);
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		local_hotel.delete();
	}

}
