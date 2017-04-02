package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class AdventureSequenceTest {
	private static final String IBAN = "BK01987654321";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;
	private final AdventureState confirmedState = new ConfirmedState();
	private final AdventureState bookRoomState = new BookRoomState();
	private final AdventureState reserveActivityState = new ReserveActivityState();

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);

	}

	// **********************************************************************/
	// ****************** CONFIRMED SEQUENCE TESTS **************************/
	// **********************************************************************/

	@Test
	public void bookRoom(@Mocked final HotelInterface hotelInterface) {

		this.adventure.setState(bookRoomState);
		Assert.assertEquals(State.BOOK_ROOM, this.adventure.getState());

		new Expectations() {
			{
				HotelInterface.reserveRoom((Type) this.any, (LocalDate) this.any, (LocalDate) this.any);
				this.result = ROOM_CONFIRMATION;
			}
		};
		this.adventure.process();
		Assert.assertEquals(ROOM_CONFIRMATION, this.adventure.getRoomConfirmation());
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void reserveActivity(@Mocked final ActivityInterface activityInterface) {

		Adventure adventureEqualDates = new Adventure(this.broker, this.begin, this.begin, 20, IBAN, 300);
		adventureEqualDates.setState(reserveActivityState);
		Assert.assertEquals(State.RESERVE_ACTIVITY, adventureEqualDates.getState());

		new Expectations() {
			{
				ActivityInterface.reserveActivity((LocalDate) this.any, (LocalDate) this.any, anyInt);
				this.result = ACTIVITY_CONFIRMATION;
			}
		};
		adventureEqualDates.process();
		Assert.assertEquals(ACTIVITY_CONFIRMATION, adventureEqualDates.getActivityConfirmation());
		Assert.assertEquals(State.CONFIRMED, adventureEqualDates.getState());
	}

	@Test
	public void reserveRoomAndActivityTest(@Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface hotelInterface, @Mocked final BankInterface bankInterface) {

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());

		new StrictExpectations() {
			{
				BankInterface.processPayment(anyString, anyInt);
				this.result = PAYMENT_CONFIRMATION;
				ActivityInterface.reserveActivity((LocalDate) this.any, (LocalDate) this.any, this.anyInt);
				this.result = ACTIVITY_CONFIRMATION;
				HotelInterface.reserveRoom((Type) this.any, (LocalDate) this.any, (LocalDate) this.any);
				this.result = ROOM_CONFIRMATION;
			}
		};

		this.adventure.process();
		Assert.assertEquals(PAYMENT_CONFIRMATION, this.adventure.getPaymentConfirmation());
		Assert.assertEquals(State.RESERVE_ACTIVITY, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(ACTIVITY_CONFIRMATION, this.adventure.getActivityConfirmation());
		Assert.assertEquals(State.BOOK_ROOM, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(ROOM_CONFIRMATION, this.adventure.getRoomConfirmation());
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	// **********************************************************************/
	// ****************** CANCELLED SEQUENCE TESTS **************************/
	// **********************************************************************/

	@Test
	public void processPaymentBankException(@Mocked BankInterface bankInterface) {

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());
		new StrictExpectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = new BankException();
			}
		};
		this.adventure.process();
		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void processPayment3RemoteAccessExceptions(@Mocked BankInterface bankInterface) {

		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = new RemoteAccessException();
				times = 3;
			}
		};
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void undoWithNoCancellations() {

		adventure.setState(State.UNDO);
		this.adventure.process();
		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void reserveActivityException(@Mocked BankInterface bankInterface,
			@Mocked ActivityInterface activityInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = PAYMENT_CONFIRMATION;
				ActivityInterface.reserveActivity((LocalDate) this.any, (LocalDate) this.any, anyInt);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();
		Assert.assertEquals(State.RESERVE_ACTIVITY, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(State.UNDO, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void reserveActivity5RemoteExceptions(@Mocked BankInterface bankInterface,
			@Mocked ActivityInterface activityInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = PAYMENT_CONFIRMATION;
				ActivityInterface.reserveActivity((LocalDate) this.any, (LocalDate) this.any, anyInt);
				this.result = new RemoteAccessException();
				times = 5;
			}
		};

		this.adventure.process();
		Assert.assertEquals(State.RESERVE_ACTIVITY, this.adventure.getState());
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		Assert.assertEquals(State.UNDO, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void bookRoom10RemoteExceptions(@Mocked BankInterface bankInterface,
			@Mocked ActivityInterface activityInterface, @Mocked HotelInterface hotelInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = PAYMENT_CONFIRMATION;
				ActivityInterface.reserveActivity((LocalDate) this.any, (LocalDate) this.any, anyInt);
				this.result = ACTIVITY_CONFIRMATION;
				HotelInterface.reserveRoom((Type) this.any, (LocalDate) this.any, (LocalDate) this.any);
				this.result = new RemoteAccessException();
				times = 10;
			}
		};

		this.adventure.process();
		Assert.assertEquals(State.RESERVE_ACTIVITY, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(State.BOOK_ROOM, this.adventure.getState());
		for (int i = 0; i < 10; i++)
			adventure.process();
		Assert.assertEquals(State.UNDO, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void bookRoomHotelException(@Mocked BankInterface bankInterface, @Mocked ActivityInterface activityInterface,
			@Mocked HotelInterface hotelInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = PAYMENT_CONFIRMATION;
				ActivityInterface.reserveActivity((LocalDate) this.any, (LocalDate) this.any, anyInt);
				this.result = ACTIVITY_CONFIRMATION;
				HotelInterface.reserveRoom((Type) this.any, (LocalDate) this.any, (LocalDate) this.any);
				this.result = new HotelException();
			}
		};

		this.adventure.process();
		Assert.assertEquals(State.RESERVE_ACTIVITY, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(State.BOOK_ROOM, this.adventure.getState());
		adventure.process();
		Assert.assertEquals(State.UNDO, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void confirmedStateRemoteAccessExceptions(@Mocked BankInterface bankInterface,
			@Mocked ActivityInterface activityInterface, @Mocked HotelInterface hotelInterface) {
		adventure.setState(new ConfirmedState());
		new Expectations() {
			{
				BankInterface.getOperationData(this.anyString);
				this.result = new RemoteAccessException();
				times = 20;
			}
		};

		for (int i = 0; i < 20; i++)
			adventure.process();
		Assert.assertEquals(State.UNDO, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(State.CANCELLED, this.adventure.getState());

		adventure.setState(new ConfirmedState());
		new Expectations() {
			{
				BankInterface.getOperationData(this.anyString);
				this.result = new BankOperationData();
				ActivityInterface.getActivityReservationData(this.anyString);
				this.result = new RemoteAccessException();
				times = 20;
			}
		};

		for (int i = 0; i < 20; i++)
			adventure.process();
		Assert.assertEquals(State.UNDO, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}
}
