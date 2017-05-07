package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;


import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData.CopyDepth;

public class RoomData {
	
	public static enum CopyDepth {
		ROOMBOOKINGS,SHALLOW
	};
	private String number;
	private Type type;
	private List<RoomBookingData> bookings = new ArrayList<>();
	public RoomData(Room room, CopyDepth depth) {
		number = room.getNumber();
		type = room.getType();
		

		switch(depth){
		case ROOMBOOKINGS:
			for (Booking booking : room.getBookingSet()) {
				this.bookings.add(new RoomBookingData(room, booking));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}
	}
	
	public RoomData(){}
	
	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<RoomBookingData> getRooms() {
		return this.bookings;
	}

	public void setRooms(List<RoomBookingData> rooms) {
		this.bookings = rooms;
	}
}
