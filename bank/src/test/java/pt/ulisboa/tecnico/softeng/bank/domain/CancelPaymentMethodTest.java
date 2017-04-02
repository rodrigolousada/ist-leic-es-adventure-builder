package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class CancelPaymentMethodTest {
	private Bank bank;
	private Account account;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		Client client = new Client(this.bank, "António");
		this.account = new Account(this.bank, client);
	}


	@Test
	public void success() {
		Operation operation = new Operation(Type.WITHDRAW, this.account, 1000);
		Operation new_operation = this.bank.getOperation(Bank.cancelPayment(operation.getReference()));
		Assert.assertTrue(new_operation.getReference().startsWith(this.bank.getCode()));
		Assert.assertTrue(new_operation.getReference().length() > Bank.CODE_SIZE);
		Assert.assertEquals(Type.DEPOSIT, new_operation.getType());
		Assert.assertEquals(1000, new_operation.getValue());
		Assert.assertEquals(operation, this.bank.getOperation(operation.getReference()));
		Assert.assertTrue(new_operation.getTime() != null);
	}
	
	@Test(expected = BankException.class)
	public void NoPaymentDone(){
		Operation operation = new Operation(Type.DEPOSIT, this.account, 1000);
		Bank.cancelPayment(operation.getReference());
	}
	
	@Test(expected = BankException.class)
	public void InvalidValuePayed(){
		Operation operation = new Operation(Type.WITHDRAW, this.account, 0);
		Bank.cancelPayment(operation.getReference());
	}
	
	@Test(expected = BankException.class)
	public void wrongReference(){
		Bank.cancelPayment("as");
	}
	
	@Test(expected = BankException.class)
	public void EmptyReference(){
		Bank.cancelPayment("");
	}
	
	@Test(expected = BankException.class)
	public void SpaceReference(){
		Bank.cancelPayment(" ");
	}
	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}
}