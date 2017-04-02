package pt.ulisboa.tecnico.softeng.broker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class ConfirmedState extends AdventureState {
	private static Logger logger = LoggerFactory.getLogger(ConfirmedState.class);

	@Override
	public State getState() {
		return State.CONFIRMED;
	}

	@Override
	public void process(Adventure adventure) {
		logger.debug("process");

		BankOperationData operation;
		try {
			operation = BankInterface.getOperationData(adventure.getPaymentConfirmation());
		} catch (BankException be) {
			this.incNumOfRemoteErrors(); 				// increment number of errors
			if(this.getNumOfRemoteErrors()==5){			// if (number of errors == 5) {
				adventure.setState(new UndoState()); 	// adventure.setState(State.UNDO);
			}											// }
			return;
		} catch (RemoteAccessException rae) {
			this.incNumOfRemoteErrors(); 				// increment number of errors
			if(this.getNumOfRemoteErrors()==20){		// if (number of errors == 20) {
				adventure.setState(new UndoState()); 	// adventure.setState(State.UNDO);
			}											// }
			return;
		}
		this.resetNumOfRemoteErrors(); 					// reset number of errors
		System.out.println("Payment confirmation: " + operation.getReference());
		System.out.println("Type: " + operation.getType());
		System.out.println("Value: " + operation.getValue());

		
		ActivityReservationData reservation;
		try {
			reservation = ActivityInterface.getActivityReservationData(adventure.getActivityConfirmation());
		} catch (ActivityException ae) {
			adventure.setState(new UndoState());
			return;
		} catch (RemoteAccessException rae) {
			this.incNumOfRemoteErrors(); 				// increment number of errors
			if(this.getNumOfRemoteErrors()==20){		// if (number of errors == 20) {
				adventure.setState(new UndoState()); 	// adventure.setState(State.UNDO);
			}											// }
			return;
		}
		this.resetNumOfRemoteErrors(); 					// reset number of errors
		System.out.println("Activity confirmation: " + reservation.getReference());
		System.out.println("Begin: " + reservation.getBegin());
		System.out.println("End: " + reservation.getEnd());
		
		if (adventure.getRoomConfirmation() != null) {
			RoomBookingData booking;
			try {
				booking = HotelInterface.getRoomBookingData(adventure.getRoomConfirmation());
			} catch (HotelException he) {
				adventure.setState(new UndoState());
				return;
			} catch (RemoteAccessException rae) {
				this.incNumOfRemoteErrors(); 				// increment number of errors
				if(this.getNumOfRemoteErrors()==20){		// if (number of errors == 20) {
					adventure.setState(new UndoState()); 	// adventure.setState(State.UNDO);
				}											// }
				return;
			}
			this.resetNumOfRemoteErrors(); 					// reset number of errors
			System.out.println("Room confirmation: " + booking.getReference());
			System.out.println("Arrival: " + booking.getArrival());
			System.out.println("Departure: " + booking.getDeparture());
		}
	}
}