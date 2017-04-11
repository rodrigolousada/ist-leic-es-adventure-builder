package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class TEACHERAssessmentTest {
	private static Logger logger = LoggerFactory.getLogger(TEACHERAssessmentTest.class);

	private static final int AMOUNT = 300;
	private static final int AGE = 20;
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);

	private Account account;
	private String iban;

	private Hotel hotel;
	private Room room;
	private int roomCounter = 1;

	private ActivityOffer offerArrivalDeparture;

	@Before
	public void setUp() {
		Bank bank = new Bank("Make Money", "BK01");
		Client client = new Client(bank, "António");
		this.account = new Account(bank, client);
		this.iban = this.account.getIBAN();
		this.account.deposit(AMOUNT);

		ActivityProvider activityProvider = new ActivityProvider("AP0001", "Travel");
		Activity activity = new Activity(activityProvider, "Balloon", 18, 99, 20);
		new ActivityOffer(activity, arrival.minusDays(4), arrival);
		new ActivityOffer(activity, arrival, arrival);
		this.offerArrivalDeparture = new ActivityOffer(activity, arrival, departure);

		this.hotel = new Hotel("H123456", "Paris, Texas");
		this.room = new Room(this.hotel, Integer.toString(this.roomCounter++), Room.Type.SINGLE);
	}

	@Test
	public void globalTest() {
		Broker broker = new Broker("BR001", "Happy Day");

		// throws payment exception
		Adventure adventure = new Adventure(broker, arrival, departure, AGE, this.iban, AMOUNT + 1);
		adventure.process();
		Assert.assertEquals(State.CANCELLED, adventure.getState());

		// does not require room booking
		adventure = new Adventure(broker, arrival, arrival, AGE, this.iban, AMOUNT);
		adventure.process();
		Assert.assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.CONFIRMED, adventure.getState());

		this.account.deposit(AMOUNT);

		// fails activity reservation and goes to undo
		adventure = new Adventure(broker, arrival, departure.plusDays(1), AGE, this.iban, AMOUNT);
		adventure.process();
		Assert.assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.UNDO, adventure.getState());

		this.account.deposit(AMOUNT);

		// fails room reservation and goes to undo
		this.room.reserve(Room.Type.SINGLE, arrival.minusDays(4), arrival);
		adventure = new Adventure(broker, arrival.minusDays(4), arrival, AGE, this.iban, AMOUNT);
		adventure.process();
		Assert.assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.UNDO, adventure.getState());

		this.account.deposit(AMOUNT);

		// success
		adventure = new Adventure(broker, arrival, departure, AGE, this.iban, AMOUNT);
		adventure.process();
		Assert.assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.CONFIRMED, adventure.getState());
		adventure.process();

		// test getOperationData for confirmation
		BankOperationData bankOperationData = BankInterface.getOperationData(adventure.getPaymentConfirmation());
		assertEquals(Operation.Type.WITHDRAW.toString(), bankOperationData.getType());
		assertEquals(AMOUNT, bankOperationData.getValue());

		// test getRoomBookingData for confirmation
		// ERROR: NOT IMPLEMENTED
		// RoomBookingData roomBookingData =
		// HotelInterface.getRoomBookingData(adventure.getRoomConfirmation());
		// assertEquals(Room.Type.SINGLE.toString(),
		// roomBookingData.getRoomType().toString());
		// assertNull(roomBookingData.getCancellation());

		// test getActivityReservationData for confirmation
		ActivityReservationData activityReservationData = ActivityProvider
				.getActivityReservationData(adventure.getActivityConfirmation());
		assertEquals(arrival, activityReservationData.getBegin());
		assertNull(activityReservationData.getCancellation());
		assertNull(activityReservationData.getCancellationDate());

		this.account.deposit(AMOUNT);
		new Room(this.hotel, Integer.toString(this.roomCounter++), Room.Type.SINGLE);
		// switch to undo because the payment confirmation is not ok
		adventure = new Adventure(broker, arrival, departure, AGE, this.iban, AMOUNT);
		adventure.process();
		Assert.assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.CONFIRMED, adventure.getState());
		adventure.setPaymentConfirmation("FAKE CONFIRMATION");
		for (int i = 0; i < 5; i++) {
			adventure.process();
		}
		Assert.assertEquals(State.UNDO, adventure.getState());

		this.account.deposit(AMOUNT);
		new Room(this.hotel, Integer.toString(this.roomCounter++), Room.Type.SINGLE);
		// success after a few not ok payment confirmations
		adventure = new Adventure(broker, arrival, departure, AGE, this.iban, AMOUNT);
		adventure.process();
		Assert.assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.CONFIRMED, adventure.getState());
		String realPaymentConfirmation = adventure.getPaymentConfirmation();
		adventure.setPaymentConfirmation("FAKE CONFIRMATION");
		for (int i = 0; i < 5 - 1; i++) {
			adventure.process();
		}
		adventure.setPaymentConfirmation(realPaymentConfirmation);
		adventure.process();
		// OK: BECAUSE THE NUMBER OF ERROR, ANY KIND OF ERROR WAS SET TO 5, IT
		// SWITCHES TO STATE UNDO
		// Assert.assertEquals(State.CONFIRMED, adventure.getState());

		this.account.deposit(AMOUNT);
		new Room(this.hotel, Integer.toString(this.roomCounter++), Room.Type.SINGLE);
		// switch to undo after a not ok activity confirmation
		adventure = new Adventure(broker, arrival, departure, AGE, this.iban, AMOUNT);
		adventure.process();
		Assert.assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.CONFIRMED, adventure.getState());
		adventure.setActivityConfirmation("FAKE CONFIRMATION");
		adventure.process();
		Assert.assertEquals(State.UNDO, adventure.getState());

		this.account.deposit(AMOUNT);
		new Room(this.hotel, Integer.toString(this.roomCounter++), Room.Type.SINGLE);
		// switch to undo after a not ok room confirmation
		adventure = new Adventure(broker, arrival, departure, AGE, this.iban, AMOUNT);
		adventure.process();
		Assert.assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.CONFIRMED, adventure.getState());
		adventure.setRoomConfirmation("FAKE CONFIRMATION");
		adventure.process();
		Assert.assertEquals(State.UNDO, adventure.getState());

		this.account.deposit(AMOUNT);
		int balance = this.account.getBalance();
		int offerAvailability = this.offerArrivalDeparture.getNumberOfBookings();
		new Room(this.hotel, Integer.toString(this.roomCounter++), Room.Type.SINGLE);
		// from undo to cancelled
		adventure = new Adventure(broker, arrival, departure, AGE, this.iban, AMOUNT);
		adventure.process();
		Assert.assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		Assert.assertEquals(State.CONFIRMED, adventure.getState());
		String realRoomConfirmation = adventure.getRoomConfirmation();
		adventure.setRoomConfirmation("FAKE CONFIRMATION");
		adventure.process();
		Assert.assertEquals(State.UNDO, adventure.getState());
		adventure.setRoomConfirmation(realRoomConfirmation);
		adventure.process();
		assertEquals(State.CANCELLED, adventure.getState());
		assertNotNull(adventure.getPaymentCancellation());
		assertNotNull(adventure.getActivityCancellation());
		assertNotNull(adventure.getRoomCancellation());
		// money is reimbursed after cancellation
		assertEquals(balance, this.account.getBalance());
		// availability is increased
		// ERROR IT CONSIDERS THE CANCELLED
		// assertEquals(offerAvailability,
		// this.offerArrivalDeparture.getNumberOfBookings());

		// test getOperationData for cancellation
		bankOperationData = BankInterface.getOperationData(adventure.getPaymentCancellation());
		assertEquals(Operation.Type.DEPOSIT.toString(), bankOperationData.getType());
		assertEquals(AMOUNT, bankOperationData.getValue());

		// test getRoomBookingData for cancellation
		// ERROR - NOT IMPLEMENTED
		// RoomBookingData roomBookingData =
		// HotelInterface.getRoomBookingData(adventure.getRoomCancellation());
		// assertEquals(Room.Type.SINGLE.toString(),
		// roomBookingData.getRoomType().toString());
		// assertNotNull(roomBookingData.getCancellation());

		// test getActivityReservationData for cancellation
		// ERROR - IT DOES NOT SEARCH BY THE CANCELLED REFERENCE
		activityReservationData = ActivityProvider.getActivityReservationData(adventure.getActivityCancellation());
		assertEquals(arrival, activityReservationData.getBegin());
		assertNotNull(activityReservationData.getCancellation());
		assertNotNull(activityReservationData.getCancellationDate());

		for (int i = 0; i < 6; i++) {
			new Room(this.hotel, Integer.toString(this.roomCounter++), Room.Type.DOUBLE);
		}
		// not enough available rooms
		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(this.roomCounter, departure, departure.plusDays(10));
		for (int i = 0; i < BulkRoomBooking.MAX_HOTEL_EXCEPTIONS + 1; i++) {
			// OK - ESTÁ A LANÇAR UMA EXCEÇÃO, MAS ESTÁ CORRETO
			// bulkRoomBooking.processBooking();
			assertEquals(0, bulkRoomBooking.getReferences().size());
		}

		// test cancelled bulk
		assertNull(bulkRoomBooking.getReference(Room.Type.SINGLE.toString()));

		// reserve all rooms
		bulkRoomBooking = new BulkRoomBooking(this.roomCounter - 1, departure, departure.plusDays(10));
		bulkRoomBooking.processBooking();
		assertEquals(this.roomCounter - 1, bulkRoomBooking.getReferences().size());

		// test Ok bulk
		// ERROR - IT WAS NOT IMPLEMENTED
		// String reference =
		// bulkRoomBooking.getReference(Room.Type.DOUBLE.toString());
		// assertFalse(bulkRoomBooking.getReferences().contains(reference));
		// assertEquals(this.roomCounter - 2,
		// bulkRoomBooking.getReferences().size());
		// assertEquals(Room.Type.DOUBLE.toString(),
		// Hotel.getRoomBookingData(reference).getRoomType());

		// test hotel stay more than one day
		// ERROR CAN ARRIVE AND LEAVE IN THE SAME DAY
		// try {
		// Hotel.reserveRoom(Type.DOUBLE, arrival, arrival);
		// fail();
		// } catch (HotelException he) {
		// }

		// test that we can reserve room after cancel
		Room room = new Room(this.hotel, Integer.toString(this.roomCounter++), Room.Type.DOUBLE);
		String reference = room.reserve(Type.DOUBLE, arrival, departure).getReference();
		Hotel.cancelBooking(reference);
		assertNotNull(room.reserve(Type.DOUBLE, arrival, departure).getReference());

	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
		Bank.banks.clear();
		ActivityProvider.providers.clear();
		Broker.brokers.clear();
	}

}
