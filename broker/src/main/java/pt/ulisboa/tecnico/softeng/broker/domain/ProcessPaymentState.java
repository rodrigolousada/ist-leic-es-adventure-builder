package pt.ulisboa.tecnico.softeng.broker.domain;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;

public class ProcessPaymentState extends AdventureState{
	
	private static Logger logger = LoggerFactory.getLogger(ProcessPaymentState.class);
	
	
	@Override
	public State getState() {
		return State.PROCESS_PAYMENT;
	}

	@Override
	public void process(Adventure adventure) {
		
		logger.debug("process");
		
		try {
			adventure.setPaymentConfirmation(BankInterface.processPayment(adventure.getIBAN(), adventure.getAmount()));
		} catch (BankException be) {
			adventure.setState(State.CANCELLED);
		} catch (RemoteAccessException rae) {
			// increment number of errors
			// if (number of errors == 3) {
			// setState(State.CANCELLED);
			// }
			return;
		}

		adventure.setState(State.RESERVE_ACTIVITY);
	}
}
