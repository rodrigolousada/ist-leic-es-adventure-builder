package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityOfferHasVacancyMethodTest {
	private ActivityOffer offer;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 3);
		
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		this.offer = new ActivityOffer(activity, begin, end);
		
	}

	@Test
	public void successZeroBookings() {
		Assert.assertTrue(this.offer.hasVacancy());
	}
	
	@Test (expected = ActivityException.class)
	public void notSucess(){
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		ActivityProvider provider2 = new ActivityProvider("XPTO", "XPTO");
		
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 1);
		
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		this.offer = new ActivityOffer(activity, begin, end);
		
		Booking booking = new Booking(provider,this.offer);
		this.offer.addBooking(booking);
		Booking booking2 = new Booking(provider2,this.offer);
		this.offer.addBooking(booking2);
		
		Assert.assertFalse(this.offer.hasVacancy());
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
