package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Adventure extends Adventure_Base {
	public static enum State {
		PROCESS_PAYMENT, RESERVE_ACTIVITY, BOOK_ROOM, UNDO, CONFIRMED, CANCELLED
	}

	private static int counter = 0;

	public Adventure(Broker broker, LocalDate begin, LocalDate end, int age, String IBAN, int amount) {
		checkArguments(broker, begin, end, age, IBAN, amount);

		setID(broker.getCode() + Integer.toString(++counter));
		setBegin(begin);
		setEnd(end);
		setAge(age);
		setIBAN(IBAN);
		setAmount(amount);

		broker.addAdventure(this);

		setBroker(broker);

		setState(State.PROCESS_PAYMENT);
	}

	public void delete() {
		setBroker(null);

		getState().delete();

		deleteDomainObject();
	}

	private void checkArguments(Broker broker, LocalDate begin, LocalDate end, int age, String IBAN, int amount) {
		if (broker == null || begin == null || end == null || IBAN == null || IBAN.trim().length() == 0) {
			throw new BrokerException();
		}

		if (end.isBefore(begin)) {
			throw new BrokerException();
		}

		if (age < 18 || age > 100) {
			throw new BrokerException();
		}

		if (amount < 1) {
			throw new BrokerException();
		}
	}

	public void setState(State state) {
		if (getState() != null) {
			getState().delete();
		}

		switch (state) {
		case PROCESS_PAYMENT:
			setState(new ProcessPaymentState());
			break;
		case RESERVE_ACTIVITY:
			setState(new ReserveActivityState());
			break;
		case BOOK_ROOM:
			setState(new BookRoomState());
			break;
		case UNDO:
			setState(new UndoState());
			break;
		case CONFIRMED:
			setState(new ConfirmedState());
			break;
		case CANCELLED:
			setState(new CancelledState());
			break;
		default:
			new BrokerException();
			break;
		}
	}

	public void process() {
		// logger.debug("process ID:{}, state:{} ", this.ID, getState().name());
		getState().process();
	}

	public boolean cancelRoom() {
		return getRoomConfirmation() != null && getRoomCancellation() == null;
	}

	public boolean cancelActivity() {
		return getActivityConfirmation() != null && getActivityCancellation() == null;
	}

	public boolean cancelPayment() {
		return getPaymentConfirmation() != null && getPaymentCancellation() == null;
	}

}
