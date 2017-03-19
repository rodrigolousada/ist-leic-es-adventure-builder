package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityOffer {
	private final LocalDate begin;
	private final LocalDate end;
	private final int capacity;
	private final Set<Booking> bookings = new HashSet<>();

	public ActivityOffer(Activity activity, LocalDate begin, LocalDate end) {
		verify(activity,begin,end);
		checkDate(begin,end);
		this.begin = begin;
		this.end = end;
		this.capacity = activity.getCapacity();

		activity.addOffer(this);
	}

	LocalDate getBegin() {
		return this.begin;
	}

	LocalDate getEnd() {
		return this.end;
	}
	
	public void verify(Activity activity, LocalDate begin, LocalDate end){
		if(activity == null){
			throw new ActivityException("Activity can't be null");
		} 
		
		if(begin == null){
			throw new ActivityException("Begin date can't be null");
		} 
		
		if(end == null){
			throw new ActivityException("End date can't be null");
		}
	}

	int getNumberOfBookings() {
		return this.bookings.size();
	}

	void addBooking(Booking booking) {
		this.bookings.add(booking);

	}

	boolean available(LocalDate begin, LocalDate end) {
		return hasVacancy() && matchDate(begin, end);
	}

	boolean matchDate(LocalDate begin, LocalDate end) {
		if (begin == null) {
			throw new ActivityException("Begin date can't be null.");
		}
		if(end == null){
			throw new ActivityException("End date can't be null");
		}

		return begin.equals(getBegin()) && end.equals(getEnd());
	}

	boolean hasVacancy() {
		return this.capacity > getNumberOfBookings();
	}
	
	public void checkDate(LocalDate begin, LocalDate end){
		if(end.isBefore(begin)){
			throw new ActivityException("Begin date must be before end date");
		}
	}

}
