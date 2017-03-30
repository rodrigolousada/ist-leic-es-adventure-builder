package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityOfferConstructorMethodTest {
	private Activity activity;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(provider, "Bush Walking", 18, 80, 25);
	}

	@Test
	public void success() {
		// define these as instance variables
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		ActivityOffer offer = new ActivityOffer(this.activity, begin, end);

		Assert.assertEquals(begin, offer.getBegin());
		Assert.assertEquals(end, offer.getEnd());
		Assert.assertEquals(1, this.activity.getNumberOfOffers());
		Assert.assertEquals(0, offer.getNumberOfBookings());
	}

	// Invalid activity
	@Test(expected = ActivityException.class)
	public void nullActivityArgument() {
		new ActivityOffer(null, new LocalDate(2016, 12, 19), new LocalDate(2016, 12, 21));
	}

	// Invalid begin date
	@Test(expected = ActivityException.class)
	public void nullBeginDateArgument() {
		new ActivityOffer(this.activity, null, new LocalDate(2016, 12, 21));
	}

	// Invalid end date
	@Test(expected = ActivityException.class)
	public void nullEndDateArgument() {
		new ActivityOffer(this.activity, new LocalDate(2016, 12, 19), null);
	}

	// One-day activity
	@Test
	public void oneDayActivity() {
		LocalDate date = new LocalDate(2016, 12, 19);

		ActivityOffer offer = new ActivityOffer(this.activity, date, date);

		Assert.assertEquals(date, offer.getBegin());
		Assert.assertEquals(date, offer.getEnd());
	}

	// Invalid activity range: end before beginning
	@Test(expected = ActivityException.class)
	public void endBeforeBeginning() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 18);

		new ActivityOffer(this.activity, begin, end);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
