package pt.ulisboa.tecnico.softeng.broker.domain;

public class BookingReference extends BookingReference_Base {

	public BookingReference() {
		super();
	}

	public BookingReference(String reference) {
		super();
		setReference(reference);
	}

	public void delete() {
		setBulkRoomBooking(null);
		deleteDomainObject();
	}
}
