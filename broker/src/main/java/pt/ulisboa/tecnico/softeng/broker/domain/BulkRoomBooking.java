package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BulkRoomBooking {
	private final Set<String> references = new HashSet<>();
	private final int number;
	private final LocalDate arrival;
	private final LocalDate departure;
	private final boolean cancelled = false;

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

	public void processBooking() {
		if (this.cancelled) {
			return;
		}

		try {
			this.references.addAll(HotelInterface.bulkBooking(this.number, this.arrival, this.departure));
		} catch (HotelException he) {
			// TODO: tries 3 consecutive times before become cancelled, it is
			// reset if it is not consecutive
		} catch (RemoteAccessException rae) {
			// TODO: tries 10 consecutive times before become cancelled, it is
			// reset if it is not consecutive
		}
	}

	public String getReference(Type type) {
		int numOfErrors = 0;
		for (String reference : this.references) {
			RoomBookingData data = null;
			try {
				data = HotelInterface.getRoomBookingData(reference);
			} catch (HotelException he) {
				// do nothing
			} catch (RemoteAccessException rae) {
				// TODO: if fails 10 consecutive times returns null, it is
				// reset if it is not consecutive
			}

			if (data != null && data.getRoomType().equals(type.name())) {
				this.references.remove(reference);
				return reference;
			}
		}
		return null;
	}
}
