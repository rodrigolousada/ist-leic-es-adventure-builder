package pt.ulisboa.tecnico.softeng.activity.services.local;

import java.util.List;
import java.util.stream.Collectors;

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

	@Atomic(mode = TxMode.READ)
	public static List<ActivityProviderData> getProviders() {
		return FenixFramework.getDomainRoot().getActivityProviderSet().stream()
				.sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).map(p -> new ActivityProviderData(p))
				.collect(Collectors.toList());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createProvider(ActivityProviderData provider) {
		new ActivityProvider(provider.getCode(), provider.getName());
	}

	@Atomic(mode = TxMode.READ)
	public static ActivityProviderData getProviderDataByCode(String code) {
		ActivityProvider provider = getProviderByCode(code);
		if (provider == null) {
			return null;
		}

		return new ActivityProviderData(provider);
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createActivity(String code, ActivityData activity) {
		ActivityProvider provider = getProviderByCode(code);
		if (provider == null) {
			throw new ActivityException();
		}

		new Activity(provider, activity.getName(), activity.getMinAge(), activity.getMaxAge(), activity.getCapacity());
	}

	@Atomic(mode = TxMode.WRITE)
	public static ActivityData getActivityDataByCode(String codeProvider, String codeActivity) {
		Activity activity = getActivityByCode(codeProvider, codeActivity);
		if (activity == null) {
			return null;
		}

		return new ActivityData(activity);
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createOffer(String codeProvider, String codeActivity, ActivityOfferData offer) {
		Activity activity = getActivityByCode(codeProvider, codeActivity);
		if (activity == null) {
			throw new ActivityException();
		}

		new ActivityOffer(activity, offer.getBegin(), offer.getEnd());
	}

	@Atomic(mode = TxMode.WRITE)
	public static String reserveActivity(LocalDate begin, LocalDate end, int age, String adventureId) {
		Booking booking = getBookingByAdventureId(adventureId);
		if (booking != null) {
			return booking.getReference();
		}

		List<ActivityOffer> offers;
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			offers = provider.findOffer(begin, end, age);
			if (!offers.isEmpty()) {
				Booking newBooking = new Booking(offers.get(0));
				newBooking.setAdventureId(adventureId);
				return newBooking.getReference();
			}
		}
		throw new ActivityException();
	}

	@Atomic(mode = TxMode.WRITE)
	public static String cancelReservation(String reference) {
		Booking booking = getBookingByReference(reference);
		if (booking != null && booking.getCancel() == null) {
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

	private static Booking getBookingByAdventureId(String adventureId) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			Booking booking = provider.getBookingByAdventureId(adventureId);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

	public static ActivityProvider getProviderByCode(String code) {
		return FenixFramework.getDomainRoot().getActivityProviderSet().stream().filter(p -> p.getCode().equals(code))
				.findFirst().orElse(null);
	}

	private static Activity getActivityByCode(String codeProvider, String codeActivity) {
		ActivityProvider provider = getProviderByCode(codeProvider);
		if (provider == null) {
			return null;
		}

		return provider.getActivitySet().stream().filter(a -> a.getCode().equals(codeActivity)).findFirst()
				.orElse(null);
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void deleteActivityProviders() {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			provider.delete();
		}
	}

}
