package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;

public class ReserveActivityState extends AdventureState {	

	@Override
	public void process(Adventure adventure) {
		try {
			adventure.setActivityConfirmation( ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(), adventure.getAge()));
		} catch (ActivityException ae) {
			adventure.setState(new UndoState());
		} catch (RemoteAccessException rae) {
			// increment number of errors
			// if (number of errors == 5) {
			// adventure.setState(new UndoState());
			// }
			// return;
		}
	
		if (adventure.getBegin().equals(adventure.getEnd())) {
			adventure.setState(new ConfirmedState());
		} else {
			adventure.setState(new BookRoomState());
		}
	}

	@Override
	public State getState() {
		return State.RESERVE_ACTIVITY;
	}
}