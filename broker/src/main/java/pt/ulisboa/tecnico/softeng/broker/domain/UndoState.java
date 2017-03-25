package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class UndoState extends AdventureState{
	private static Logger logger = LoggerFactory.getlogger(UndoState.class)
		
	@Override
	public State getState(){
		return State.UNDO;
	}
	
	@Override
	
	public void process(Adventure adventure){
		logger.debug("process");
		
		if(adventure.cancelPayment()){
			try {
				adventure.paymentCancellation = BankInterface.cancelPayment(getPaymentConfirmation());
			} catch (HotelException | RemoteAccessException ex) {
				return;
			}
		}
		
		if (adventure.cancelActivity()) {
			try {
				adventure.activityCancellation = ActivityInterface.cancelReservation(getActivityConfirmation());
			} catch (HotelException | RemoteAccessException ex) {
				return;
			}
		}

		if (adventure.cancelRoom()) {
			try {
				adventure.roomCancellation = HotelInterface.cancelBooking(getRoomConfirmation());
			} catch (HotelException | RemoteAccessException ex) {
				return;
			}
		}

		if (!adventure.cancelPayment() && !adventure.cancelActivity() && !adventure.cancelRoom()) {
			adventure.setState(State.CANCELLED);
		}
	} 
}