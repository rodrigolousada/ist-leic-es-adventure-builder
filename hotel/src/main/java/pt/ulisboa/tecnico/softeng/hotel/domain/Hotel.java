package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final Set<Room> rooms = new HashSet<>();

	public Hotel(String code, String name) {
		checkArguments(code, name);

		this.code = code;
		this.name = name;
		Hotel.hotels.add(this);
	}

	private void checkArguments(String code, String name) {
		if (code == null || name == null || code.trim().length() == 0 || name.trim().length() == 0) {
			throw new HotelException();
		}

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}

		for (Hotel hotel : hotels) {
			if (hotel.getCode().equals(code)) {
				throw new HotelException();
			}
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		for (Room room : this.rooms) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
		}
		return null;
	}

	String getCode() {
		return this.code;
	}

	String getName() {
		return this.name;
	}

	void addRoom(Room room) {
		if (hasRoom(room.getNumber())) {
			throw new HotelException();
		}

		this.rooms.add(room);
	}

	int getNumberOfRooms() {
		return this.rooms.size();
	}

	public boolean hasRoom(String number) {
		for (Room room : this.rooms) {
			if (room.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	private static Hotel getHotelByCode(String code) {
		for (Hotel hotel : hotels) {
			if (hotel.getCode().equals(code)) {
				return hotel;
			}
		}
		throw new HotelException();
	}

	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		throw new HotelException();
	}

	public static Booking getBooking(String reference) {
		Hotel hotel = getHotelByCode(reference.substring(0, CODE_SIZE));
		Booking result;
		for (Room room : hotel.rooms) {
			result = room.getBooking(reference);
			if (result != null)
				return result;
		}
		return null;
	}

	// returning same reference used for room confirmation
	public static String cancelBooking(String roomConfirmation) {

		if (roomConfirmation == null || roomConfirmation.length() <= CODE_SIZE)
			throw new HotelException();

		String hotelcode = roomConfirmation.substring(0, CODE_SIZE);
		Hotel hotelbooked = getHotelByCode(hotelcode);

		for (Room room : hotelbooked.rooms) {
			if (room.getBooking(roomConfirmation) != null)
				return room.cancelBooking(roomConfirmation);
		}

		throw new HotelException();
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		// TODO implement
		throw new HotelException();
	}

	public int getNumberOfFreeRoomsOfHotel(LocalDate arrival, LocalDate departure) {
		int total = 0;
		for (Room room : this.rooms) {
			if (room.isFree(room.getType(), arrival, departure)) total++; 
		}
		return total;
	}
	
	public static int NumberOfFreeRooms(LocalDate arrival, LocalDate departure) {
		int total = 0;
		for (Hotel hotel : Hotel.hotels) {
			total += hotel.getNumberOfFreeRoomsOfHotel(arrival, departure); 
		}
		return total;
	}
	
	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		if(number <= 0 || arrival == null || departure == null || arrival.isAfter(departure) || NumberOfFreeRooms(arrival,departure) < number) {
			throw new HotelException();
		}
		
		Set<String> bookings = new HashSet<>();
		
		try{ //should not dependent of Type.SINGLE or Type.DOUBLE
			for(;number>0; number--) {
				String booking = reserveRoom(Type.SINGLE, arrival, departure);
				bookings.add(booking);
			}
		}
		catch(HotelException he) {
			for(;number>0; number--) {
				String booking = reserveRoom(Type.DOUBLE, arrival, departure);
				bookings.add(booking);
			}
		}
		return bookings;
	}

}
