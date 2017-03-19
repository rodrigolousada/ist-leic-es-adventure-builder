package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityOfferConstructorMethodTest {
	private static final int CAPACITY = 25;
	private static final int MAX_AGE = 50;
	private static final int MIN_AGE = 25;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Activity activity;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(provider, "Bush Walking", MIN_AGE, MAX_AGE, CAPACITY);
	}

	@Test
	public void success() {
		ActivityOffer offer = new ActivityOffer(this.activity, this.begin, this.end);

		Assert.assertEquals(this.begin, offer.getBegin());
		Assert.assertEquals(this.end, offer.getEnd());
		Assert.assertEquals(1, this.activity.getNumberOfOffers());
		Assert.assertEquals(0, offer.getNumberOfBookings());
	}

	@Test(expected = ActivityException.class)
	public void nullActivity() {
		new ActivityOffer(null, this.begin, this.end);
	}

	@Test(expected = ActivityException.class)
	public void nullbeginDate() {
		new ActivityOffer(this.activity, null, this.end);
	}

	@Test(expected = ActivityException.class)
	public void nullEndDate() {
		new ActivityOffer(this.activity, this.begin, null);
	}

	@Test
	public void successBeginDateEqualEndDate() {
		ActivityOffer offer = new ActivityOffer(this.activity, this.begin, this.begin);

		Assert.assertEquals(this.begin, offer.getBegin());
		Assert.assertEquals(this.begin, offer.getEnd());
		Assert.assertEquals(1, this.activity.getNumberOfOffers());
		Assert.assertEquals(0, offer.getNumberOfBookings());
	}

	@Test(expected = ActivityException.class)
	public void endDateImmediatelyBeforeBeginDate() {
		new ActivityOffer(this.activity, this.begin, this.begin.minusDays(1));
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
