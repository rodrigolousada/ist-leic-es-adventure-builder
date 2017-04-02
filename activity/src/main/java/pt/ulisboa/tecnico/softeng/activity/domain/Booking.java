package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class Booking {
	private static int counter = 0;

	private final String reference;
	private boolean cancelled;
	private LocalDate cancellationDate;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		checkArguments(provider, offer);

		this.reference = provider.getCode() + Integer.toString(++Booking.counter);

		offer.addBooking(this);
	}

	private void checkArguments(ActivityProvider provider, ActivityOffer offer) {
		if (provider == null || offer == null) {
			throw new ActivityException();
		}

	}

	public String getReference() {
		return this.reference;
	}

	LocalDate getCancellationDate() {
		return this.cancellationDate;
	}

	public String cancel() {
		this.cancelled = true;
		this.cancellationDate = new LocalDate();
		return this.getCancellationReference();
	}

	public String getCancellationReference() {
		if (this.cancelled) {
			return this.reference + "/cancel";
		}
		return null;
	}
}
