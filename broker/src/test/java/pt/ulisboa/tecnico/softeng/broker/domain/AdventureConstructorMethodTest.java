package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureConstructorMethodTest {
	private Broker broker;
	private LocalDate begin;
	private LocalDate end;

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");
		this.begin = new LocalDate(2016, 12, 19);
		this.end = new LocalDate(2016, 12, 21);
	}

	@Test
	public void success() {

		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, "BK011234567", 300);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals("BK011234567", adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getBankPayment());
		Assert.assertNull(adventure.getActivityBooking());
		Assert.assertNull(adventure.getRoomBooking());
	}

	@Test
	public void singleDayAdventure() {
		LocalDate end = this.begin;

		// it is not necessary to define the variable
		Adventure adventure = new Adventure(this.broker, this.begin, end, 20, "BK011234567", 300);
	}

	@Test(expected = BrokerException.class)
	public void invalidDate() {
		LocalDate badEnd = this.begin.minusDays(1);

		Adventure adventure = new Adventure(this.broker, this.begin, badEnd, 20, "BK011234567", 300);
	}

	@Test(expected = BrokerException.class)
	public void nullBegin() {
		Adventure adventure = new Adventure(this.broker, null, this.end, 20, "BK011234567", 300);
	}

	@Test(expected = BrokerException.class)
	public void nullEnd() {
		Adventure adventure = new Adventure(this.broker, this.begin, null, 20, "BK011234567", 300);
	}

	@Test(expected = BrokerException.class)
	public void nullBroker() {
		Adventure adventure = new Adventure(null, this.begin, this.end, 20, "BK011234567", 300);
	}

	@Test(expected = BrokerException.class)
	public void underAge() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 17, "BK011234567", 300);
	}

	@Test(expected = BrokerException.class)
	public void overAge() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 100, "BK011234567", 300);
	}

	@Test
	public void edgeAges() {
		int low = 18;
		int high = 99;
		// one test test one single thing
		Adventure adventure1 = new Adventure(this.broker, this.begin, this.end, low, "BK011234567", 300);
		Adventure adventure2 = new Adventure(this.broker, this.begin, this.end, high, "BK011234567", 300);
		Assert.assertEquals(low, adventure1.getAge());
		Assert.assertEquals(high, adventure2.getAge());
	}

	@Test(expected = BrokerException.class)
	public void nullIBAN() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, null, 300);
	}

	@Test(expected = BrokerException.class)
	public void badAmount() {
		int amount = 0;
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, "BK011234567", amount);
	}

	@Test
	public void edgeAmount() {
		int amount = 1;
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, "BK011234567", amount);
		Assert.assertEquals(amount, adventure.getAmount());
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
