package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityProviderConstructorMethodTest {

	@Test
	public void success() {
		ActivityProvider provider = new ActivityProvider("XtremX", "Adventure++");

		Assert.assertEquals("Adventure++", provider.getName());
		Assert.assertTrue(provider.getCode().length() == ActivityProvider.CODE_SIZE);
		Assert.assertEquals(1, ActivityProvider.providers.size());
		Assert.assertEquals(0, provider.getNumberOfActivities());
	}

	@Test(expected = ActivityException.class)
	public void nullName() {
		new ActivityProvider(null, "Adventure++");
	}

	@Test(expected = ActivityException.class)
	public void nullCode() {
		new ActivityProvider("XtremX", null);
	}

	@Test
	public void codeLengthFailure() {
		try {
			new ActivityProvider("XtremXYY", "Adventure++");
			Assert.fail();
		} catch (ActivityException e) {
			Assert.assertEquals(0, ActivityProvider.getNumberOfProviders());
		}
	}

	@Test
	public void codeUniqueFailure() {
		ActivityProvider provider = new ActivityProvider("XtremX", "Adventure++");

		try {
			new ActivityProvider("XtremX", "Adventure+-");
			Assert.fail();
		} catch (ActivityException e) {
			Assert.assertEquals(1, ActivityProvider.getNumberOfProviders());
		}
	}

	@Test
	public void nameUniqueFailure() {
		ActivityProvider provider = new ActivityProvider("XtremX", "Adventure++");

		try {
			new ActivityProvider("XtremY", "Adventure++");
			Assert.fail();
		} catch (ActivityException e) {
			Assert.assertEquals(1, ActivityProvider.getNumberOfProviders());
		}
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
