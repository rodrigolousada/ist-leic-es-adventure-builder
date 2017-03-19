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

public class CancelledState extends AdventureState {
	private static Logger logger = LoggerFactory.getLogger(CancelledState.class);

	@Override
	public State getState() {
		return State.CANCELLED;
	}

	@Override
	public void process(Adventure adventure) {
		logger.debug("process");

		BankOperationData operation;
		if (adventure.getPaymentCancellation() != null) {
			try {
				operation = BankInterface.getOperationData(adventure.getPaymentConfirmation());
			} catch (BankException | RemoteAccessException e) {
				return;
			}
			System.out.println("Payment confirmation: " + operation.getReference());
			System.out.println("Type: " + operation.getType());
			System.out.println("Value: " + operation.getValue());

			try {
				operation = BankInterface.getOperationData(adventure.getPaymentCancellation());
			} catch (BankException | RemoteAccessException e) {
				return;
			}
			System.out.println("Payment cancellation " + operation.getReference());
			System.out.println("Type: " + operation.getType());
			System.out.println("Value: " + operation.getValue());
		}

		if (adventure.getActivityCancellation() != null) {
			ActivityReservationData data;
			try {
				data = ActivityInterface.getActivityReservationData(adventure.getActivityCancellation());
			} catch (ActivityException | RemoteAccessException e) {
				return;
			}
			System.out.println("Activity confirmation: " + data.getReference());
			System.out.println("Begin: " + data.getBegin());
			System.out.println("End: " + data.getEnd());
			System.out.println("Activity cancellation: " + data.getCancellation());
			System.out.println("Cancellation date: " + data.getCancellationDate());

		}

		if (adventure.getRoomCancellation() != null) {
			RoomBookingData data;
			try {
				data = HotelInterface.getRoomBookingData(adventure.getRoomCancellation());
			} catch (HotelException | RemoteAccessException e) {
				return;
			}
			System.out.println("Room confirmation: " + data.getReference());
			System.out.println("Arrival: " + data.getArrival());
			System.out.println("Departure: " + data.getDeparture());
			System.out.println("Room cancellation: " + data.getCancellation());
			System.out.println("Cancellation date: " + data.getCancellationDate());
		}

	}

}
