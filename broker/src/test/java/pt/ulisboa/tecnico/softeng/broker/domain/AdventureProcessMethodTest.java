package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureProcessMethodTest {
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 19);
	private Broker broker;
	private String IBAN;
	private final static int ACCOUNT_BALANCE = 1000;

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");

		Bank bank = new Bank("Money", "BK01");
		Client client = new Client(bank, "António");
		Account account = new Account(bank, client);
		this.IBAN = account.getIBAN();
		account.deposit(ACCOUNT_BALANCE);

		Hotel hotel = new Hotel("XPTO123", "Paris");
		new Room(hotel, "01", Type.SINGLE);

		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(provider, "Bush Walking", 19, 80, 3);
		new ActivityOffer(activity, this.begin, this.end);
	}

	@Test(expected = BrokerException.class)
	public void paymentFail() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, ACCOUNT_BALANCE + 1);
		adventure.process();
	}

	@Test(expected = BrokerException.class)
	public void roomReservationFail() {
		// There is only one room, so a second reservation should fail.
		Adventure adventure1 = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, ACCOUNT_BALANCE / 2 - 1);
		Adventure adventure2 = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, ACCOUNT_BALANCE / 2 - 1);

		adventure1.process();
		adventure2.process();
	}

	@Test(expected = BrokerException.class)
	public void activityReservationFail() {
		// Under the age limit
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 18, this.IBAN, ACCOUNT_BALANCE - 1);

		adventure.process();
	}

	@Test
	public void success() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, ACCOUNT_BALANCE - 1);

		adventure.process();

		Assert.assertNotNull(adventure.getBankPayment());
		Assert.assertNotNull(adventure.getRoomBooking());
		Assert.assertNotNull(adventure.getActivityBooking());
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
		Hotel.hotels.clear();
		ActivityProvider.providers.clear();
		Broker.brokers.clear();
	}
}
