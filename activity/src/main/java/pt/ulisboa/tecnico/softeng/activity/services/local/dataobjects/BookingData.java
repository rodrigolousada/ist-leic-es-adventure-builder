package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ulisboa.tecnico.softeng.activity.domain.Booking;

public class BookingData {

	String reference;
	String cancel;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate cancellationDate;
	
	public BookingData(Booking booking) {
		this.setReference(booking.getReference());
		this.setCancel(booking.getCancel());
		this.setCancellationDate(booking.getCancellationDate());
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getCancel() {
		return cancel;
	}

	public void setCancel(String cancel) {
		this.cancel = cancel;
	}

	public LocalDate getCancellationDate() {
		return cancellationDate;
	}

	public void setCancellationDate(LocalDate cancellationDate) {
		this.cancellationDate = cancellationDate;
	}
}
