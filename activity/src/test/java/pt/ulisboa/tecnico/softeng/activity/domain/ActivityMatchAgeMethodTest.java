package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityMatchAgeMethodTest {
	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 80;
	private static final int CAPACITY = 30;
	private Activity activity;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(provider, "Bush Walking", MIN_AGE, MAX_AGE, CAPACITY);
	}

	@Test
	public void success() {
		Assert.assertTrue(this.activity.matchAge((MAX_AGE - MIN_AGE) / 2));
	}

	@Test
	public void successEqualMinAge() {
		Assert.assertTrue(this.activity.matchAge(MIN_AGE));
	}

	public void lessThanMinAge() {
		Assert.assertFalse(this.activity.matchAge(MIN_AGE - 1));
	}

	public void successEqualMaxAge() {
		Assert.assertFalse(this.activity.matchAge(MAX_AGE));
	}

	public void greaterThanMaxAge() {
		Assert.assertFalse(this.activity.matchAge(MAX_AGE + 1));
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
