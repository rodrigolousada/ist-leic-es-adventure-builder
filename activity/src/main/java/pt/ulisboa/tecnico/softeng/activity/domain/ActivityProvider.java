package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProvider {
	public static Set<ActivityProvider> providers = new HashSet<>();

	static final int CODE_SIZE = 6;

	private final String name;
	private final String code;
	private final Set<Activity> activities = new HashSet<>();

	public ActivityProvider(String code, String name) {
		checkArguments(code, name);

		this.code = code;
		this.name = name;

		ActivityProvider.providers.add(this);
	}

	private void checkArguments(String code, String name) {
		if (code == null || name == null || code.trim().equals("") || name.trim().equals("")) {
			throw new ActivityException();
		}

		if (code.length() != ActivityProvider.CODE_SIZE) {
			throw new ActivityException();
		}

		for (ActivityProvider activityProvider : providers) {
			if (activityProvider.getCode().equals(code) || activityProvider.getName().equals(name)) {
				throw new ActivityException();
			}
		}
	}

	String getName() {
		return this.name;
	}

	String getCode() {
		return this.code;
	}

	int getNumberOfActivities() {
		return this.activities.size();
	}

	void addActivity(Activity activity) {
		this.activities.add(activity);
	}

	public List<ActivityOffer> findOffer(LocalDate begin, LocalDate end, int age) {
		List<ActivityOffer> result = new ArrayList<>();
		for (Activity activity : this.activities) {
			result.addAll(activity.getOffers(begin, end, age));
		}
		return result;
	}

	public static String reserveActivity(LocalDate begin, LocalDate end, int age) {
		List<ActivityOffer> offers;
		for (ActivityProvider provider : ActivityProvider.providers) {
			offers = provider.findOffer(begin, end, age);
			if (!offers.isEmpty()) {
				return new Booking(provider, offers.get(0)).getReference();
			}
		}
		throw new ActivityException();
	}

	private static ActivityProvider getProviderByCode(String code) {
		for (ActivityProvider provider : ActivityProvider.providers) {
			if (provider.getCode().equals(code)) {
				return provider;
			}
		}

		throw new ActivityException();
	}

	public static String cancelReservation(String activityConfirmation) {
		if (activityConfirmation == null || activityConfirmation.trim().equals("")
				|| activityConfirmation.length() <= ActivityProvider.CODE_SIZE) {
			throw new ActivityException();
		}

		String providerCode = activityConfirmation.substring(0, CODE_SIZE);
		ActivityProvider provider = getProviderByCode(providerCode);
		for (Activity activity : provider.activities) {
			for (ActivityOffer offer : activity.getOfferSet()) {
				for (Booking booking : offer.getBookings()) {
					// actually we are using a cancellation
					if (booking.getReference().equals(activityConfirmation)) {
						return booking.cancel();
					}
				}
			}
		}
		throw new ActivityException("No such reservation.");
	}

	public static ActivityReservationData getActivityReservationData(String reference) {
		if (reference == null) {
			throw new ActivityException("Null reference.");
		}

		String providerCode = reference.substring(0, CODE_SIZE);
		ActivityProvider provider = getProviderByCode(providerCode);
		for (Activity activity : provider.activities) {
			for (ActivityOffer offer : activity.getOfferSet()) {
				for (Booking booking : offer.getBookings()) {
					// ERROR - IT IS NECESSARY TO COMPARE WITH THE CANCELLED
					// REFERENCE AS WELL BUT THEY ARE USING THE SAME
					if (booking.getReference().equals(reference)) {
						return new ActivityReservationData(booking.getReference(), booking.getCancellationReference(),
								activity.getName(), activity.getCode(), offer.getBegin(), offer.getEnd(),
								booking.getCancellationDate());
					}
				}
			}
		}

		throw new ActivityException("No such reservation.");
	}

}
