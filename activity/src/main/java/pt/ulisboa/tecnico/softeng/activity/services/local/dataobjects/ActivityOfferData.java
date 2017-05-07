package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;

public class ActivityOfferData {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate begin;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate end;
	int capacity;
	List<BookingData> bookings;
	
	public ActivityOfferData(ActivityOffer offer) {
		this.setBegin(offer.getBegin());
		this.setEnd(offer.getEnd());
		this.setCapacity(offer.getCapacity());
		
		this.bookings = new ArrayList<BookingData>();
		for (Booking booking : offer.getBookingSet()){
			bookings.add(new BookingData(booking));
		}
		
	}

	public ActivityOfferData() {
	}

	public LocalDate getBegin() {
		return begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}
