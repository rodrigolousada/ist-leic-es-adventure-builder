package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BulkRoomBooking extends BulkRoomBooking_Base {
	public static final int MAX_HOTEL_EXCEPTIONS = 3;
	public static final int MAX_REMOTE_ERRORS = 10;

	private int numberOfHotelExceptions = 0;
	private int numberOfRemoteErrors = 0;

	public BulkRoomBooking(int number, LocalDate arrival, LocalDate departure) {
		setNumber(number);
		setArrival(arrival);
		setDeparture(departure);
	}

	public Set<String> getReferences() {
		return getReferenceSet().stream().map(r -> r.getReference()).collect(Collectors.toSet());
	}

	public void processBooking() {
		if (getCancelled()) {
			return;
		}

		try {
			for (String reference : HotelInterface.bulkBooking(getNumber(), getArrival(), getDeparture())) {
				addReference(new BookingReference(reference));
			}

			this.numberOfHotelExceptions = 0;
			this.numberOfRemoteErrors = 0;
			return;
		} catch (HotelException he) {
			this.numberOfHotelExceptions++;
			if (this.numberOfHotelExceptions == MAX_HOTEL_EXCEPTIONS) {
				setCancelled(true);
			}
			this.numberOfRemoteErrors = 0;
			return;
		} catch (RemoteAccessException rae) {
			this.numberOfRemoteErrors++;
			if (this.numberOfRemoteErrors == MAX_REMOTE_ERRORS) {
				setCancelled(true);
			}
			this.numberOfHotelExceptions = 0;
			return;
		}
	}

	public String getReference(String type) {
		if (getCancelled()) {
			return null;
		}

		for (BookingReference br : getReferenceSet()) {
			RoomBookingData data = null;
			try {
				data = HotelInterface.getRoomBookingData(br.getReference());
				this.numberOfRemoteErrors = 0;
			} catch (HotelException he) {
				this.numberOfRemoteErrors = 0;
			} catch (RemoteAccessException rae) {
				this.numberOfRemoteErrors++;
				if (this.numberOfRemoteErrors == MAX_REMOTE_ERRORS) {
					setCancelled(true);
				}
			}

			if (data != null && data.getRoomType().equals(type)) {
				this.getReferenceSet().remove(br);
				return br.getReference();
			}
		}
		return null;
	}

	public void delete() {
		for (BookingReference b : getReferenceSet()) {
			b.delete();
		}

		setBroker(null);

		deleteDomainObject();
	}
}
