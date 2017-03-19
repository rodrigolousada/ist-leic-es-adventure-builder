package pt.ulisboa.tecnico.softeng.broker.interfaces;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;

public class ActivityInterface {
	public static String reserveActivity(LocalDate begin, LocalDate end, int age) {
		return ActivityProvider.reserveActivity(begin, end, age);
	}

	public static String cancelReservation(String activityConfirmation) {
		return ActivityProvider.cancelReservation(activityConfirmation);
	}

	public static ActivityReservationData getActivityReservationData(String reference) {
		return ActivityProvider.getActivityReservationData(reference);
	}
}
