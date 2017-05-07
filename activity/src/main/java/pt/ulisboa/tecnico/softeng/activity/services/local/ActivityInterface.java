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
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData.CopyDepth;
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

	@Atomic(mode = TxMode.WRITE)
	public static void createActivityProvider(ActivityProviderData providerData) {
		new ActivityProvider(providerData.getCode(), providerData.getName());
	}

	@Atomic(mode = TxMode.READ)
	public static ActivityProviderData getProviderDataByCode(String providerCode, CopyDepth depth) {
		ActivityProvider activityProvider = getActivityProviderByCode(providerCode);

		if (activityProvider != null) {
			return new ActivityProviderData(activityProvider, depth);
		} else
			return null;
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createActivity(String providerCode, ActivityData activityData) {
		new Activity(getActivityProviderByCode(providerCode), activityData.getName(), activityData.getMinAge(),
				activityData.getMaxAge(), activityData.getCapacity());
	}

	private static ActivityProvider getActivityProviderByCode(String code) {
		for (ActivityProvider activityProvider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			if (activityProvider.getCode().equals(code)) {
				return activityProvider;
			}
		}
		return null;
	}
}
