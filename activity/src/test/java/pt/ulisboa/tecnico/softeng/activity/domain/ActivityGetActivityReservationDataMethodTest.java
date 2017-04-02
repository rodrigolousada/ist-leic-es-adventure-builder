package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;

public class ActivityGetActivityReservationDataMethodTest {
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

	@Test
	public void plainTest() {
		ActivityReservationData ard = ActivityProvider.getActivityReservationData(this.booking.getReference());

		Assert.assertEquals(ard.getReference(), this.booking.getReference());
		Assert.assertEquals(ard.getName(), this.activity.getName());
		Assert.assertEquals(ard.getCode(), this.activity.getCode());
		Assert.assertEquals(ard.getBegin(), this.offer.getBegin());
		Assert.assertEquals(ard.getEnd(), this.offer.getEnd());
		Assert.assertNull(ard.getCancellation());
		Assert.assertNull(ard.getCancellationDate());
	}

	@Test
	public void cancelledTest() {
		this.booking.cancel();
		String cancelReference = this.booking.getCancellationReference();

		ActivityReservationData ard = ActivityProvider.getActivityReservationData(this.booking.getReference());

		Assert.assertEquals(ard.getReference(), this.booking.getReference());
		Assert.assertEquals(ard.getName(), this.activity.getName());
		Assert.assertEquals(ard.getCode(), this.activity.getCode());
		Assert.assertEquals(ard.getBegin(), this.offer.getBegin());
		Assert.assertEquals(ard.getEnd(), this.offer.getEnd());
		Assert.assertEquals(ard.getCancellation(), cancelReference);
		Assert.assertEquals(ard.getCancellationDate(), booking.getCancellationDate());
	}

	@Test(expected = ActivityException.class)
	public void nullReference() {
		ActivityProvider.getActivityReservationData(null);
	}

	@Test(expected = ActivityException.class)
	public void inexistentReference() {
		ActivityProvider.getActivityReservationData(this.booking.getReference() + "0");
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
