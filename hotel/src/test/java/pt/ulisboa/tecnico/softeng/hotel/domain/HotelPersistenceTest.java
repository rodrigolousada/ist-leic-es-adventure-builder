package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class HotelPersistenceTest {
	private static final String HOTEL_CODE = "HOTEL01";
	private static final String HOTEL_NAME = "Ritz";
	private static final String ROOM_NUMBER = "010";
	private static final Room.Type ROOM_TYPE = Room.Type.SINGLE;

	private Hotel hotel;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		this.hotel = new Hotel(HOTEL_CODE, HOTEL_NAME);

		new Room(this.hotel, ROOM_NUMBER, ROOM_TYPE);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		assertEquals(1, FenixFramework.getDomainRoot().getHotelSet().size());

		List<Hotel> hotels = new ArrayList<>(FenixFramework.getDomainRoot().getHotelSet());
		Hotel hotel = hotels.get(0);

		assertEquals(HOTEL_CODE, hotel.getCode());
		assertEquals(HOTEL_NAME, hotel.getName());
		assertEquals(1, hotel.getRoomSet().size());

		List<Room> rooms = new ArrayList<>(hotel.getRoomSet());
		Room room = rooms.get(0);

		assertEquals(ROOM_NUMBER, room.getNumber());
		assertEquals(ROOM_TYPE, room.getType());

	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}

}
