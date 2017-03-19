package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProviderConstructorMethodTest {
	private static final String PROVIDER_CODE = "XtremX";
	private static final String PROVIDER_NAME = "Adventure++";

	@Test
	public void success() {
		ActivityProvider provider = new ActivityProvider(PROVIDER_CODE, PROVIDER_NAME);

		Assert.assertEquals(PROVIDER_NAME, provider.getName());
		Assert.assertTrue(provider.getCode().length() == ActivityProvider.CODE_SIZE);
		Assert.assertEquals(1, ActivityProvider.providers.size());
		Assert.assertEquals(0, provider.getNumberOfActivities());
	}

	@Test(expected = ActivityException.class)
	public void nullCode() {
		new ActivityProvider(null, PROVIDER_NAME);
	}

	@Test(expected = ActivityException.class)
	public void emptyCode() {
		new ActivityProvider("      ", PROVIDER_NAME);
	}

	@Test(expected = ActivityException.class)
	public void nullName() {
		new ActivityProvider(PROVIDER_CODE, null);
	}

	@Test(expected = ActivityException.class)
	public void emptyName() {
		new ActivityProvider(PROVIDER_CODE, "    ");
	}

	@Test(expected = ActivityException.class)
	public void fiveCharCode() {
		new ActivityProvider("12345", PROVIDER_NAME);
	}

	@Test(expected = ActivityException.class)
	public void sevenCharCode() {
		new ActivityProvider("1234567", PROVIDER_NAME);
	}

	@Test
	public void noteUniqueCode() {
		new ActivityProvider(PROVIDER_CODE, PROVIDER_NAME);

		try {
			new ActivityProvider(PROVIDER_CODE, "Hello");
			fail();
		} catch (ActivityException ae) {
			Assert.assertEquals(1, ActivityProvider.providers.size());
		}
	}

	@Test
	public void noteUniqueName() {
		new ActivityProvider(PROVIDER_CODE, PROVIDER_NAME);

		try {
			new ActivityProvider("123456", PROVIDER_NAME);
			fail();
		} catch (ActivityException ae) {
			Assert.assertEquals(1, ActivityProvider.providers.size());
		}
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
