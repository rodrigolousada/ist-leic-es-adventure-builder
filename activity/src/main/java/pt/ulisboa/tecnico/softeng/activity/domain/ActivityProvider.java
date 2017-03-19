package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityProvider {
	public static Set<ActivityProvider> providers = new HashSet<>();

	static final int CODE_SIZE = 6;

	private final String name;
	private final String code;
	private final Set<Activity> activities = new HashSet<>();

	public ActivityProvider(String code, String name) {
		verify(code, name);
		checkCode(code);
		this.code = code;
		checkName(name);
		this.name = name;

		ActivityProvider.providers.add(this);
	}

	private void checkCode(String code) {
		if (code.length() != ActivityProvider.CODE_SIZE) {
			throw new ActivityException();
		}
		for (ActivityProvider provider : ActivityProvider.providers) {
			if (provider.getCode().equals(code)) {
				throw new ActivityException();
			}
		}
	}

	private void checkName(String name) {
		for (ActivityProvider provider : ActivityProvider.providers) {
			if (provider.getName().equals(name)) {
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

	static int getNumberOfProviders() {
		return ActivityProvider.providers.size();
	}

	void addActivity(Activity activity) {
		this.activities.add(activity);
	}

	public Set<ActivityOffer> findOffer(LocalDate begin, LocalDate end, int age) throws ActivityException {
		if (begin == null || end == null || end.isBefore(begin)) {
			throw new ActivityException();
		}
		Set<ActivityOffer> result = new HashSet<>();
		for (Activity activity : this.activities) {
			result.addAll(activity.getOffers(begin, end, age));
		}
		return result;
	}

	public static String reserveActivity(LocalDate begin, LocalDate end, int age) {
		Set<ActivityOffer> offers;
		for (ActivityProvider provider : ActivityProvider.providers) {
			offers = provider.findOffer(begin, end, age);
			if (!offers.isEmpty()) {
				for (ActivityOffer offer : offers) {
					return new Booking(provider, offer).getReference();
				}
			}
		}
		return null;
	}

	private void verify(String code, String name) {

		if (code == null) {
			throw new ActivityException("Code must not be null");
		}

		if (name == null) {
			throw new ActivityException("Name must not be null");
		}
	}

}
