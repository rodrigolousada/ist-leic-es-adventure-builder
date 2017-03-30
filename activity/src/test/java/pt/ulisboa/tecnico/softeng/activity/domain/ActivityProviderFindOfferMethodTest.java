package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityProviderFindOfferMethodTest {
	private ActivityProvider provider;
	private ActivityOffer offer;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 25);

		// define as instance variables just that they can be reused in the
		// tests
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		this.offer = new ActivityOffer(activity, begin, end);
	}

	@Test
	public void success() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Set<ActivityOffer> offers = this.provider.findOffer(begin, end, 40);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer));
	}

	@Test
	public void dateFailure() {
		LocalDate end = new LocalDate(2016, 12, 19);
		LocalDate begin = new LocalDate(2016, 12, 21);
		// it is not necessary to initialize the set
		Set<ActivityOffer> offers = new HashSet<>();
		try {
			offers = this.provider.findOffer(begin, end, 40);
			Assert.fail();
		} catch (ActivityException e) {
			Assert.assertEquals(0, offers.size());
		}
	}

	// necessary to test illegal age, empty activity set, empty activity offer
	// set, return more than one offer

	@Test(expected = ActivityException.class)
	public void nullBegin() {
		LocalDate end = new LocalDate(2016, 12, 19);
		this.provider.findOffer(null, end, 40);
	}

	@Test(expected = ActivityException.class)
	public void nullEnd() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		this.provider.findOffer(begin, null, 40);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
