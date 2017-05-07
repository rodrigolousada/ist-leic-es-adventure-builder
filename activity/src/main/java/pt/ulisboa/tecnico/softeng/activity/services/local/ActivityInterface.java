package pt.ulisboa.tecnico.softeng.activity.services.local;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityReservationData;

public class ActivityInterface {

	@Atomic(mode = TxMode.WRITE)
	public static String reserveActivity(LocalDate begin, LocalDate end, int age) {
		List<ActivityOffer> offers;
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			offers = provider.findOffer(begin, end, age);
			if (!offers.isEmpty()) {
				return new Booking(offers.get(0)).getReference();
			}
		}
		throw new ActivityException();
	}

	@Atomic(mode = TxMode.WRITE)
	public static String cancelReservation(String reference) {
		Booking booking = getBookingByReference(reference);
		if (booking != null) {
			return booking.cancel();
		}
		throw new ActivityException();
	}

	@Atomic(mode = TxMode.READ)
	public static ActivityReservationData getActivityReservationData(String reference) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			for (Activity activity : provider.getActivitySet()) {
				for (ActivityOffer offer : activity.getActivityOfferSet()) {
					Booking booking = offer.getBooking(reference);
					if (booking != null) {
						return new ActivityReservationData(provider, offer, booking);
					}
				}
			}
		}
		throw new ActivityException();
	}

	private static Booking getBookingByReference(String reference) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			Booking booking = provider.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

	@Atomic(mode = TxMode.READ)
	public static List<ActivityProviderData> getActivityProviders() {
		List<ActivityProviderData> providers = new ArrayList<>();
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			providers.add(new ActivityProviderData(provider, ActivityProviderData.CopyDepth.SHALLOW));
		}
		return providers;
	}
	
	@Atomic(mode = TxMode.READ)
	public static List<ActivityOfferData> getActivityOffers(String providerCode, String activityCode) {
		Activity activity = getActivityByCode(providerCode, activityCode);
		List<ActivityOfferData> offers = new ArrayList<>();
		for (ActivityOffer offer : activity.getActivityOfferSet()) {
			offers.add(new ActivityOfferData(offer));
		}
		return offers;
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createActivityProvider(ActivityProviderData providerData) {
		new ActivityProvider(providerData.getCode(), providerData.getName());
	}

	private static ActivityProvider getActivityProviderByCode(String code) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			if (provider.getCode().equals(code)) {
				return provider;
			}
		}
		return null;
	}

	private static Activity getActivityByCode(String providerCode, String activityCode) {
		ActivityProvider provider = getActivityProviderByCode(providerCode);

		if (provider == null)
			return null;

		for (Activity activity : provider.getActivitySet()) {
			if (activity.getCode().equals(activityCode)) {
				return activity;
			}
		}
		return null;
	}

	public static List<ActivityData> getActivities(String providerCode) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createActivityOffer(String providerCode, String activityCode, ActivityOfferData offerData) {
		Activity activity = getActivityByCode(providerCode, activityCode);
		if(activity == null){
			throw new ActivityException("No such activity.");
		}
		new ActivityOffer(activity, offerData.getBegin(), offerData.getEnd());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createActivity(String providerCode, ActivityData activity) {
		// TODO Auto-generated method stub
		ActivityProvider provider = getActivityProviderByCode(providerCode);
		if(provider == null)
			throw new ActivityException("No such provider: " + providerCode);
		
		new Activity(provider, activity.getName(), activity.getMinAge(), activity.getMaxAge(), activity.getCapacity());
	}
	
}
