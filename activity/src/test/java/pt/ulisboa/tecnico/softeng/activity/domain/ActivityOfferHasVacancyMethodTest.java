package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class ActivityOfferHasVacancyMethodTest extends RollbackTestAbstractClass {
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
		new Booking(this.provider, this.offer);
		Assert.assertTrue(this.offer.hasVacancy());
	}

	@Test
	public void bookingIsFull() {
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
		Assert.assertFalse(this.offer.hasVacancy());
	}

	@Test
	public void bookingIsFullMinusOne() {
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
		Assert.assertTrue(this.offer.hasVacancy());
	}

	@Test
	public void hasCancelledBookings() {
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
		Booking booking = new Booking(this.provider, this.offer);
		booking.cancel();

		Assert.assertTrue(this.offer.hasVacancy());
	}

	@Test
	public void hasCancelledBookingsButFull() {
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
		Booking booking = new Booking(this.provider, this.offer);
		booking.cancel();
		new Booking(this.provider, this.offer);

		Assert.assertFalse(this.offer.hasVacancy());
	}
}
