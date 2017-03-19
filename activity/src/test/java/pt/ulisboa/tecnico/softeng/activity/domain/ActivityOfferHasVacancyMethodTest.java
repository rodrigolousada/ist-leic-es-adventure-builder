package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityOfferHasVacancyMethodTest {
	private ActivityProvider provider;
	private ActivityOffer offer;

	@Before
	public void setUp() {
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

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
