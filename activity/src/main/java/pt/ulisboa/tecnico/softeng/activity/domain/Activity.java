package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class Activity {
	private static final int MIN_AGE = 18;
	private static final int MAX_AGE = 100;

	private static int counter = 0;

	private final String name;
	private final String code;
	private final int minAge;
	private final int maxAge;
	private final int capacity;
	private final Set<ActivityOffer> offers = new HashSet<>();

	public Activity(ActivityProvider provider, String name, int minAge, int maxAge, int capacity) {
		checkArguments(provider, name, minAge, maxAge, capacity);

		this.code = provider.getCode() + Integer.toString(++Activity.counter);
		this.name = name;
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.capacity = capacity;

		provider.addActivity(this);
	}

	private void checkArguments(ActivityProvider provider, String name, int minAge, int maxAge, int capacity) {
		if (provider == null || name == null || name.trim().equals("")) {
			throw new ActivityException();
		}

		if (minAge < MIN_AGE || maxAge >= MAX_AGE || minAge > maxAge) {
			throw new ActivityException();
		}

		if (capacity < 1) {
			throw new ActivityException();
		}

	}

	String getName() {
		return this.name;
	}

	String getCode() {
		return this.code;
	}

	int getMinAge() {
		return this.minAge;
	}

	int getMaxAge() {
		return this.maxAge;
	}

	int getCapacity() {
		return this.capacity;
	}

	int getNumberOfOffers() {
		return this.offers.size();
	}
	
	Set<ActivityOffer> getOfferSet() {
		return this.offers;
	}
	
	void addOffer(ActivityOffer offer) {
		this.offers.add(offer);
	}

	Set<ActivityOffer> getOffers(LocalDate begin, LocalDate end, int age) {
		Set<ActivityOffer> result = new HashSet<>();
		for (ActivityOffer offer : this.offers) {
			if (matchAge(age) && offer.available(begin, end)) {
				result.add(offer);
			}
		}		
		return result;
	}

	boolean matchAge(int age) {
		return age >= this.minAge && age <= this.maxAge;
	}

}
