package pt.ulisboa.tecnico.softeng.activity.domain;

import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityMatchAgeMethodTest {
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private Activity activity;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(provider, "Bush Walking", 18, 80, 3);
	}

	// each test case should be tested in isolation

	@Test
	public void successIn() {
		Assert.assertTrue(this.activity.matchAge(50));
		Assert.assertTrue(this.activity.matchAge(18));
		Assert.assertTrue(this.activity.matchAge(80));
	}

	@Test
	public void notsucess() {
		Assert.assertFalse(this.activity.matchAge(17));
		Assert.assertFalse(this.activity.matchAge(81));
		Assert.assertFalse(this.activity.matchAge(100));
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
