package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class BookingContructorMethodTest extends RollbackTestAbstractClass {
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

		Assert.assertTrue(booking.getReference().startsWith(this.provider.getCode()));
		Assert.assertTrue(booking.getReference().length() > ActivityProvider.CODE_SIZE);
		Assert.assertEquals(1, this.offer.getNumberOfBookings());
	}

	@Test(expected = ActivityException.class)
	public void nullProvider() {
		new Booking(null, this.offer);
	}

	@Test(expected = ActivityException.class)
	public void nullOffer() {
		new Booking(this.provider, null);
	}

	@Test
	public void bookingEqualCapacity() {
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
		try {
			new Booking(this.provider, this.offer);
			fail();
		} catch (ActivityException ae) {
			Assert.assertEquals(3, this.offer.getNumberOfBookings());
		}
	}

	@Test
	public void bookingEqualCapacityButHasCancelled() {
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
		Booking booking = new Booking(this.provider, this.offer);
		booking.cancel();
		new Booking(this.provider, this.offer);

		Assert.assertEquals(3, this.offer.getNumberOfBookings());
	}
}
