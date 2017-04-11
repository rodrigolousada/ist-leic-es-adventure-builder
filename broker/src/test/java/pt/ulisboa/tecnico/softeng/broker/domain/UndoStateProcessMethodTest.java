package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;

@RunWith(JMockit.class)
public class UndoStateProcessMethodTest {
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

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(new UndoState());
	}

	// ERROR - ALL TESTS FAIL

	// IT IS NECESSARY TO DEFINE THE STATE CORRECTELY, FOR INSTANCE, SET THE
	// VALUES OF CONFIRMATION AND CANCELLATION

	@Test
	public void cancelTest(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface hotelInterface) {

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());

		new Verifications() {
			{
				BankInterface.cancelPayment(this.anyString);
				this.times = 1;

				ActivityInterface.cancelReservation(this.anyString);
				this.times = 1;

				HotelInterface.cancelBooking(this.anyString);
				this.times = 1;
			}
		};
	}

	@Test
	public void cancelPaymentHotelException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentCancellation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void cancelPaymentRemoteAccessException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentCancellation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void cancelPayment(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentCancellation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void cancelActivity(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityCancellation(ACTIVITY_CONFIRMATION);

		new StrictExpectations() {
			{
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void cancelRoom(@Mocked final HotelInterface hotelInterface) {
		this.adventure.setRoomCancellation(ROOM_CANCELLATION);

		new StrictExpectations() {
			{
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

}