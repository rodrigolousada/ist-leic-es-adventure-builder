package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityCancelReservationMethodTest {
	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 80;
	private static final int CAPACITY = 25;
	private static final int AGE = 40;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	private ActivityProvider provider;
	private Activity activity;
	private ActivityOffer offer;
	private Booking booking;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(this.provider, "Bush Walking", MIN_AGE, MAX_AGE, CAPACITY);

		this.offer = new ActivityOffer(this.activity, this.begin, this.end);
		this.booking = new Booking(this.provider, this.offer);
	}

	@Test(expected = ActivityException.class)
	public void nullActivityConfirmation() {
		this.provider.cancelReservation(null);
	}

	@Test(expected = ActivityException.class)
	public void emptyActivityConfirmation() {
		this.provider.cancelReservation("   ");
	}
	
	@Test(expected = ActivityException.class)
	public void invalidActivityConfirmation() {
		this.provider.cancelReservation("12345");
	}
	
	@Test(expected = ActivityException.class)
	public void inexistentProvider() {
		this.provider.cancelReservation("1234567");
	}
	
	@Test(expected = ActivityException.class)
	public void inexistentBooking() {
		this.provider.cancelReservation("XtremX25");
	}
	
	@Test
	public void success() {
		String cancelReference = this.provider.cancelReservation(this.booking.getReference());
		
		Assert.assertEquals(cancelReference, this.booking.getCancellationReference());
		Assert.assertEquals(new LocalDate(), this.booking.getCancellationDate());
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
