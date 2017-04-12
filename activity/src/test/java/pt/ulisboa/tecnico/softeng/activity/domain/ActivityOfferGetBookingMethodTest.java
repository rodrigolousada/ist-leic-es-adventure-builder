package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.Test;

public class ActivityOfferGetBookingMethodTest extends RollbackTestAbstractClass {
	private ActivityProvider provider;
	private ActivityOffer offer;

	@Override
	public void populate4Test() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 3);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		this.offer = new ActivityOffer(activity, begin, end);
	}

	@Test
	public void success() {
		Booking booking = new Booking(this.provider, this.offer);

		assertEquals(booking, this.offer.getBooking(booking.getReference()));
	}

	@Test
	public void successCancelled() {
		Booking booking = new Booking(this.provider, this.offer);
		booking.cancel();

		assertEquals(booking, this.offer.getBooking(booking.getCancellation()));
	}

	@Test
	public void doesNotExist() {
		new Booking(this.provider, this.offer);

		assertNull(this.offer.getBooking("XPTO"));
	}
}
