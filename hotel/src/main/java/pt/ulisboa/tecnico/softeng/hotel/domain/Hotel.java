package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel extends Hotel_Base {
	static final int CODE_SIZE = 7;

	public Hotel(String code, String name) {
		checkArguments(code, name);

		setCode(code);
		setName(name);

		FenixFramework.getDomainRoot().addHotel(this);
	}

	public void delete() {
		setRoot(null);

		for (Room room : getRoomSet()) {
			room.delete();
		}

		deleteDomainObject();
	}

	private void checkArguments(String code, String name) {
		if (code == null || name == null || code.trim().length() == 0 || name.trim().length() == 0) {
			throw new HotelException();
		}

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}

		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			if (hotel.getCode().equals(code)) {
				throw new HotelException();
			}
		}
	}

	@Override
	public int getCounter() {
		int counter = super.getCounter() + 1;
		setCounter(counter);
		return counter;
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		for (Room room : getRoomSet()) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
		}
		return null;
	}

	public Set<Room> getAvailableRooms(LocalDate arrival, LocalDate departure) {
		Set<Room> availableRooms = new HashSet<>();
		for (Room room : getRoomSet()) {
			if (room.isFree(room.getType(), arrival, departure)) {
				availableRooms.add(room);
			}
		}
		return availableRooms;
	}

	@Override
	public void addRoom(Room room) {
		if (hasRoom(room.getNumber())) {
			throw new HotelException();
		}

		super.addRoom(room);
	}

	public boolean hasRoom(String number) {
		for (Room room : getRoomSet()) {
			if (room.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	public Booking getBooking(String reference) {
		for (Room room : getRoomSet()) {
			Booking booking = room.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

}
