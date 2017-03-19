package pt.ulisboa.tecnico.softeng.activity.domain;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class Booking {
	private static int counter = 0;

	private final String reference;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		verify(provider, offer);
		this.reference = provider.getCode() + Integer.toString(++Booking.counter);
		checkCapacity(offer);
		offer.addBooking(this);
	}

	private void checkCapacity(ActivityOffer offer) {
		if (!offer.hasVacancy()) {
			throw new ActivityException();
		}
	}

	public String getReference() {
		return this.reference;
	}

	private void verify(ActivityProvider provider, ActivityOffer offer) {

		if (provider == null) {
			throw new ActivityException("Provider must not be null");
		}

		if (offer == null) {
			throw new ActivityException("Offer must not be null");
		}
	}
}
