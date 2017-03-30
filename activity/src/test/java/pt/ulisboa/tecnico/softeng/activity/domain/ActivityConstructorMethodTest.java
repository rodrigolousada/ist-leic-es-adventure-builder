package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityConstructorMethodTest {
	private ActivityProvider provider;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
	}

	@Test
	public void success() {
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 25);

		Assert.assertTrue(activity.getCode().startsWith(this.provider.getCode()));
		Assert.assertTrue(activity.getCode().length() > ActivityProvider.CODE_SIZE);
		Assert.assertEquals("Bush Walking", activity.getName());
		Assert.assertEquals(18, activity.getMinAge());
		Assert.assertEquals(80, activity.getMaxAge());
		Assert.assertEquals(25, activity.getCapacity());
		Assert.assertEquals(0, activity.getNumberOfOffers());
		Assert.assertEquals(1, this.provider.getNumberOfActivities());
	}

	// didn't test for spaces in name and empty name

	@Test(expected = ActivityException.class)
	public void nullProvider() {
		new Activity(null, "test", 18, 80, 25);

	}

	@Test(expected = ActivityException.class)
	public void nullName() {
		new Activity(this.provider, null, 18, 80, 25);

	}

	@Test(expected = ActivityException.class)
	public void underAge() {
		// declare constants for name, age, and capacity, and use them in the
		// different tests, for instance MIN_AGE - 1
		new Activity(this.provider, "test4", 17, 80, 25);

	}

	@Test(expected = ActivityException.class)
	public void overAge() {
		// do not use a border value for min age
		new Activity(this.provider, "test5", 18, 100, 25);

	}

	@Test
	public void agesOnTheLimits() {
		// should be two different tests
		Activity activity = new Activity(this.provider, "test7", 18, 99, 1);
		Assert.assertEquals(18, activity.getMinAge());
		Assert.assertEquals(99, activity.getMaxAge());
	}

	@Test(expected = ActivityException.class)
	public void capacityZero() {
		new Activity(this.provider, "test6", 20, 80, 0);
	}

	@Test
	public void minimumCapacity() {
		Activity activity = new Activity(this.provider, "test7", 20, 80, 1);
		Assert.assertEquals(1, activity.getCapacity());
	}

	@Test(expected = ActivityException.class)
	public void underAgeOverAge() {
		// this is an inconsistent ages because min > max
		new Activity(this.provider, "test7", 81, 80, 23);
	}

	@Test
	public void singleAllowedAge() {
		Activity activity = new Activity(this.provider, "test7", 80, 80, 23);
		Assert.assertEquals(80, activity.getMinAge());
		Assert.assertEquals(80, activity.getMaxAge());
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
