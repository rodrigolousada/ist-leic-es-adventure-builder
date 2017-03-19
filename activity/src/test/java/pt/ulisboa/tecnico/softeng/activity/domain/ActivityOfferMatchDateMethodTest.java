package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityOfferMatchDateMethodTest {
	private ActivityOffer offer;
	private LocalDate begin;
	private LocalDate end;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 3);

		this.begin = new LocalDate(2016, 12, 19);
		this.end = new LocalDate(2016, 12, 21);

		this.offer = new ActivityOffer(activity, begin, end);
	}

	@Test
	public void success() {
		Assert.assertTrue(this.offer.matchDate(begin, end));
	}

	@Test
	public void differentBeginning() {
		Assert.assertFalse(this.offer.matchDate(begin.minusDays(1), end));
	}

	@Test
	public void differentEnd() {
		Assert.assertFalse(this.offer.matchDate(begin, end.plusDays(1)));
	}

	@Test
	public void differentBeginningAndEnd() {
		Assert.assertFalse(this.offer.matchDate(begin.minusDays(1), end.plusDays(1)));
	}

	// Error cases
	@Test(expected = ActivityException.class)
	public void nullBegin() {
		this.offer.matchDate(null, end);
	}

	@Test(expected = ActivityException.class)
	public void nullEnd() {
		this.offer.matchDate(begin, null);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
