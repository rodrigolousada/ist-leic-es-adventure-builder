package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class BookingContructorMethodTest {
	private ActivityProvider provider;
	private ActivityOffer offer;
	private Activity activity;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(this.provider, "Bush Walking", 18, 80, 1);

		// define as instance variables to reuse them in the tests
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		this.offer = new ActivityOffer(this.activity, begin, end);
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
	public void success() {
		Booking booking = new Booking(this.provider, this.offer);

		Assert.assertTrue(booking.getReference().startsWith(this.provider.getCode()));
		Assert.assertTrue(booking.getReference().length() > ActivityProvider.CODE_SIZE);
		Assert.assertEquals(1, this.offer.getNumberOfBookings());
	}

	// not interesting to have a test for capacity 1
	@Test
	public void capacityFailure() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		// it is already created... you do not need another...
		this.offer = new ActivityOffer(this.activity, begin, end);
		new Booking(this.provider, this.offer);
		try {
			new Booking(this.provider, this.offer);
			Assert.fail();
		} catch (ActivityException e) {
			// this assert is irrelevant
			Assert.assertEquals(1, this.activity.getCapacity());
		}
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
