package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.Atomic.TxMode;

public class BookingPersistenceTest {
	private static final String PROVIDER_NAME = "ExtremeAdventure";
	private static final String PROVIDER_CODE = "XtremX";
	private static final String ACTIVITY_NAME = "Bush Walking";
	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 50;
	private static final int CAPACITY = 30;

	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	
	private ActivityProvider local_provider;
	private Activity local_activity;
	private ActivityOffer local_offer;
	

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		this.local_provider = new ActivityProvider(PROVIDER_CODE, PROVIDER_NAME);
		this.local_activity = new Activity(local_provider, ACTIVITY_NAME, MIN_AGE, MAX_AGE, CAPACITY);
		this.local_offer = new ActivityOffer(this.local_activity, this.begin, this.end);
		
		new Booking(local_provider, local_offer);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		assertEquals(1, FenixFramework.getDomainRoot().getActivityProviderSet().size());

		List<ActivityProvider> providers = new ArrayList<>(FenixFramework.getDomainRoot().getActivityProviderSet());
		ActivityProvider activityProvider = providers.get(0);

		assertEquals(PROVIDER_CODE, activityProvider.getCode());
		assertEquals(PROVIDER_NAME, activityProvider.getName());
		assertEquals(1, activityProvider.getActivitySet().size());

		List<Activity> activities = new ArrayList<>(activityProvider.getActivitySet());
		Activity activity = activities.get(0);

		assertNotNull(activity.getCode());
		assertEquals(activityProvider, activity.getActivityProvider());
		assertEquals(ACTIVITY_NAME, activity.getName());
		assertEquals(MIN_AGE, activity.getMinAge());
		assertEquals(MAX_AGE, activity.getMaxAge());
		assertEquals(CAPACITY, activity.getCapacity());
		
		List<ActivityOffer> offers = new ArrayList<>(activity.getActivityOfferSet());
		ActivityOffer offer = offers.get(0);
		
		assertEquals(activity, offer.getActivity());
		assertEquals(this.begin, offer.getBegin());
		assertEquals(this.end, offer.getEnd());
		assertEquals(CAPACITY, offer.getCapacity());
		
		List<Booking> bookings = new ArrayList<>(offer.getBookingSet());
		Booking booking = bookings.get(0);
		
		assertEquals(offer, booking.getActivityOffer());
		assertNotNull(booking.getReference());
		assertNull(booking.getCancellationDate());
		assertNull(booking.getCancellation());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (ActivityProvider activityProvider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			activityProvider.delete();
		}
	}
}