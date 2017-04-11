package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BulkRoomBooking {
	private final Set<String> references = new HashSet<>();
	private final int number;
	private final LocalDate arrival;
	private final LocalDate departure;
	private int numberOfHotelExceptions = 0;
	private int numberOfRemoteErrors = 0;
	public final static int MAX_HOTEL_EXCEPTIONS = 5;
	private final static int MAX_REMOTE_ERRORS = 5;
	private boolean cancelled = false;

	public BulkRoomBooking(int number, LocalDate arrival, LocalDate departure) {
		this.number = number;
		this.arrival = arrival;
		this.departure = departure;
	}

	public Set<String> getReferences() {
		return this.references;
	}

	public int getNumber() {
		return this.number;
	}

	public LocalDate getArrival() {
		return this.arrival;
	}

	public LocalDate getDeparture() {
		return this.departure;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public boolean isDone() {
		return !this.references.isEmpty();
	}

	/**
	 * @return a <code>boolean</code> indicating if the booking was successfully
	 *         processed
	 * @exception BrokerException
	 *                if the booking is cancelled
	 */
	public boolean processBooking() throws BrokerException {
		if (this.isCancelled()) {
			if (this.numberOfHotelExceptions == MAX_HOTEL_EXCEPTIONS) {
				throw new BrokerException("BulkRoomBooking cancelled: too many HotelExceptions");
			}
			if (this.numberOfRemoteErrors == MAX_REMOTE_ERRORS) {
				throw new BrokerException("BulkRoomBooking cancelled: too many RemoteAccessExceptions");
			}
			throw new BrokerException("BulkRoomBooking cancelled: unknown reason");
		}

		if (this.isDone()) {
			return true;
		}

		try {
			this.references.addAll(HotelInterface.bulkBooking(this.number, this.arrival, this.departure));
			this.numberOfHotelExceptions = 0;
			this.numberOfRemoteErrors = 0;
			return true;
		} catch (HotelException he) {
			this.numberOfHotelExceptions++;
			if (this.numberOfHotelExceptions == MAX_HOTEL_EXCEPTIONS) {
				this.cancelled = true;
				throw new BrokerException("BulkRoomBooking cancelled: too many HotelExceptions");
			}
			this.numberOfRemoteErrors = 0;
			return false;
		} catch (RemoteAccessException rae) {
			this.numberOfRemoteErrors++;
			if (this.numberOfRemoteErrors == MAX_REMOTE_ERRORS) {
				this.cancelled = true;
				throw new BrokerException("BulkRoomBooking cancelled: too many RemoteAccessExceptions");
			}
			this.numberOfHotelExceptions = 0;
			return false;
		}
	}

	public String getReference(String type) {
		if (this.cancelled) {
			return null;
		}

		for (String reference : this.references) {
			RoomBookingData data = null;
			try {
				data = HotelInterface.getRoomBookingData(reference);
				// this.numberOfRemoteErrors = 0;
			} catch (HotelException he) {
				// this.numberOfRemoteErrors = 0;
			} catch (RemoteAccessException rae) {
				// this.numberOfRemoteErrors++;
				// if (this.numberOfRemoteErrors == MAX_REMOTE_ERRORS) {
				// this.cancelled = true;
				// }
			}

			if (data != null && data.getRoomType().equals(type)) {
				this.references.remove(reference);
				return reference;
			}
		}
		return null;
	}
}
