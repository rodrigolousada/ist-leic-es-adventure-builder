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
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class ProcessPaymentStateProcessMethod {
	private static final String IBAN = "BK01987654321";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(State.PROCESS_PAYMENT);
	}
	
	@Test
	public void ProcessPaymentTest(@Mocked final BankInterface bankInterface){
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, 300);
				this.result = PAYMENT_CONFIRMATION;
			}
		};

		this.adventure.process();
		
		Assert.assertEquals(PAYMENT_CONFIRMATION, this.adventure.getPaymentConfirmation());
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
	
	}
	
	@Test
	public void ProcessPaymentRemoteAccessException(@Mocked final BankInterface bankInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, 300);
				this.result=new RemoteAccessException();
			}
		};
		
		this.adventure.process();

		Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventure.getState());
	}
	
	@Test
	public void ProcessPaymentThreeRemoteExceptions(@Mocked final BankInterface bankInterface) {
		
		for(int i=1; i<=5; i++){
			new StrictExpectations() {
				{
					BankInterface.processPayment(IBAN, 300);
					this.result=new RemoteAccessException();
				}
			};
				
			this.adventure.process(); 
		}

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void ProcessPaymentOneBankException(@Mocked final BankInterface bankInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, 300);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
}