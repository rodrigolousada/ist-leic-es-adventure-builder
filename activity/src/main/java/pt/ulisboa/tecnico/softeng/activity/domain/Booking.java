package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class Booking extends Booking_Base{
	private static int counter = 0;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		checkArguments(provider, offer);

		setReference(provider.getCode() + Integer.toString(++Booking.counter));

		offer.addBookingToOffer(this);
	}

	public void delete() {	
		setActivityOffer(null);
		
		deleteDomainObject();
	}
	
	private void checkArguments(ActivityProvider provider, ActivityOffer offer) {
		if (provider == null || offer == null) {
			throw new ActivityException();
		}
	}

	public String getCancellation() {
		return getCancel();
	}

	public void setCancellation(String cancellation) {
		setCancel(cancellation);
	}

	public String cancel() {
		setCancel("CANCEL" + getReference());
		setCancellationDate(new LocalDate());
		return getCancel();
	}

	public boolean isCancelled() {
		return getCancel() != null;
	}
}
