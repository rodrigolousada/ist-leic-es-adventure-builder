package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureConstructorMethodTest {
	private static final int AGE = 20;
	private static final int AMOUNT = 300;
	private static final String IBAN = "BK011234567";
	private Broker broker;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");
	}

	@Test
	public void success() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, AGE, IBAN, AMOUNT);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void nullBroker() {
		new Adventure(null, this.begin, this.end, AGE, IBAN, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void nullBegin() {
		new Adventure(this.broker, null, this.end, AGE, IBAN, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void nullEnd() {
		new Adventure(this.broker, this.begin, null, AGE, IBAN, AMOUNT);
	}

	@Test
	public void successEqual18() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 18, IBAN, AMOUNT);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(18, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void lessThan18Age() {
		new Adventure(this.broker, this.begin, this.end, 17, IBAN, AMOUNT);
	}

	@Test
	public void successEqual100() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 100, IBAN, AMOUNT);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(100, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void over100() {
		new Adventure(this.broker, this.begin, this.end, 101, IBAN, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void nullIBAN() {
		new Adventure(this.broker, this.begin, this.end, AGE, null, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void emptyIBAN() {
		new Adventure(this.broker, this.begin, this.end, AGE, "     ", AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void negativeAmount() {
		new Adventure(this.broker, this.begin, this.end, AGE, IBAN, -100);
	}

	@Test
	public void success1Amount() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, AGE, IBAN, 1);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(1, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void zeroAmount() {
		new Adventure(this.broker, this.begin, this.end, AGE, IBAN, 0);
	}

	@Test
	public void successEqualDates() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.begin, AGE, IBAN, AMOUNT);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.begin, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void inconsistentDates() {
		new Adventure(this.broker, this.begin, this.begin.minusDays(1), AGE, IBAN, AMOUNT);
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
