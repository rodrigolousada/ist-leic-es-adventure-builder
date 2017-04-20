package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Booking extends Booking_Base {
	private static int counter = 0;

	Booking(Hotel hotel, LocalDate arrival, LocalDate departure) {
		checkArguments(hotel, arrival, departure);

		super.setReference(hotel.getCode() + Integer.toString(++Booking.counter));
		super.setArrival(arrival);
		super.setDeparture(departure);
	}

	public void delete() {
		super.getRoom().removeBooking(this);
		deleteDomainObject();
	}

	private void checkArguments(Hotel hotel, LocalDate arrival, LocalDate departure) {
		if (hotel == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}
	}

	public String getReference() {
		return super.getReference();
	}

	public String getCancellation() {
		return super.getCancellation();
	}

	public LocalDate getArrival() {
		return super.getArrival();
	}

	public LocalDate getDeparture() {
		return super.getDeparture();
	}

	public LocalDate getCancellationDate() {
		return super.getCancellationDate();
	}

	boolean conflict(LocalDate arrival, LocalDate departure) {
		if (isCancelled()) {
			return false;
		}

		if (arrival.equals(departure)) {
			return true;
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}

		if ((arrival.equals(super.getArrival()) || arrival.isAfter(super.getArrival()))
				&& arrival.isBefore(super.getDeparture())) {
			return true;
		}

		if ((departure.equals(super.getDeparture()) || departure.isBefore(super.getDeparture()))
				&& departure.isAfter(super.getArrival())) {
			return true;
		}

		if ((arrival.isBefore(super.getArrival()) && departure.isAfter(super.getDeparture()))) {
			return true;
		}

		return false;
	}

	public String cancel() {
		this.setCancellation(super.getReference() + "CANCEL");
		this.setCancellationDate(new LocalDate());
		return super.getCancellation();
	}

	public boolean isCancelled() {
		return super.getCancellation() != null;
	}

}
