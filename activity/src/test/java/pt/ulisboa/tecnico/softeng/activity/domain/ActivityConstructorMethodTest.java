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
	
	@Test (expected = ActivityException.class)
	public void under_age(){
		new Activity(this.provider, "test", 17, 80, 25);
		
	}
	
	@Test (expected = ActivityException.class)
	public void over_age(){
		new Activity(this.provider, "test1", 18, 100, 25);
		
	}
	@Test (expected = ActivityException.class)
	public void capacity_zero(){
		new Activity(this.provider, "test2", 20, 80, 0);
	}
	
	@Test (expected = ActivityException.class)
	public void under_age_over_age(){
		new Activity(this.provider, "test3", 80, 20, 23);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
