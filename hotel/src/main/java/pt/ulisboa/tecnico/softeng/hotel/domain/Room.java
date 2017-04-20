package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Room extends Room_Base {
	public static enum Type {
		SINGLE, DOUBLE
	}

	public Room(Hotel hotel, String number, Type type) {
		checkArguments(hotel, number, type);

		super.setNumber(number);
		super.setType(type);
		hotel.addRoom(this);
	}

	// TODO : DELETE BOOKINGS
	public void delete() {
		super.getHotel().removeRoom(this);
		deleteDomainObject();
	}

	private void checkArguments(Hotel hotel, String number, Type type) {
		if (hotel == null || number == null || number.trim().length() == 0 || type == null) {
			throw new HotelException();
		}

		if (!number.matches("\\d*")) {
			throw new HotelException();
		}
	}

	public Hotel getHotel() {
		return super.getHotel();
	}

	public String getNumber() {
		return super.getNumber();
	}

	public Type getType() {
		return super.getType();
	}

	int getNumberOfBookings() {
		return super.getBookingSet().size();
	}

	boolean isFree(Type type, LocalDate arrival, LocalDate departure) {
		if (!type.equals(super.getType())) {
			return false;
		}

		for (Booking booking : super.getBookingSet()) {
			if (booking.conflict(arrival, departure)) {
				return false;
			}
		}

		return true;
	}

	public Booking reserve(Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		if (!isFree(type, arrival, departure)) {
			throw new HotelException();
		}

		Booking booking = new Booking(super.getHotel(), arrival, departure);
		this.addBooking(booking);

		return booking;
	}

	public Booking getBooking(String reference) {
		for (Booking booking : super.getBookingSet()) {
			if (booking.getReference().equals(reference)
					|| (booking.isCancelled() && booking.getCancellation().equals(reference))) {
				return booking;
			}
		}
		return null;
	}

}