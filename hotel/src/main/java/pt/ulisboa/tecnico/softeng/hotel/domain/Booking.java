package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Booking {
	private static int counter = 0;

	private final String reference;
	private final LocalDate arrival;
	private final LocalDate departure;

	Booking(Hotel hotel, LocalDate arrival, LocalDate departure) {
		checkDate(arrival, departure);
		this.reference = hotel.getCode() + Integer.toString(++Booking.counter);

		this.arrival = arrival;
		this.departure = departure;
	}

	private void checkDate(LocalDate arrival, LocalDate departure) {
		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}
	}

	public String getReference() {
		return this.reference;
	}

	LocalDate getArrival() {
		return this.arrival;
	}

	LocalDate getDeparture() {
		return this.departure;
	}

	boolean conflict(LocalDate arrival, LocalDate departure) {

		checkDate(arrival, departure);

		if (arrival.isAfter(this.arrival) && arrival.isBefore(this.departure)) {
			return true;
		}

		if (departure.isAfter(this.arrival) && departure.isBefore(this.departure)) {
			return true;
		}

		if (arrival.isBefore(this.arrival) && departure.isAfter(this.departure)) {
			return true;
		}

		if (arrival.isEqual(this.arrival) && departure.isAfter(this.arrival)) {
			return true;
		}

		if (arrival.isBefore(this.departure) && departure.isEqual(this.departure)) {
			return true;
		}

		if (arrival.isEqual(this.arrival) && departure.isEqual(this.departure)) {
			return true;
		}

		return false;
	}

}
