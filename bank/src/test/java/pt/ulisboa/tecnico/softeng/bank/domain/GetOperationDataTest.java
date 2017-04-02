package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class GetOperationDataTest {
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
		Operation operation = new Operation(Type.DEPOSIT, this.account, 1000);
		BankOperationData data = Bank.getOperationData(operation.getReference());
		Assert.assertTrue(data.getReference().startsWith(this.bank.getCode()));
		Assert.assertTrue(data.getReference().length() > Bank.CODE_SIZE);
		Assert.assertEquals(String.valueOf(Type.DEPOSIT), data.getType());
		Assert.assertEquals(operation.getAccount().getIBAN(), data.getIban());
		Assert.assertEquals(1000, data.getValue());
		Assert.assertTrue(data.getTime() != null);
		Assert.assertEquals(operation, this.bank.getOperation(operation.getReference()));
	}
	
	@Test(expected = BankException.class)
	public void nullReference(){
		Bank.getOperationData(null);
	}
	
	@Test(expected = BankException.class)
	public void wrongReference(){
		Bank.getOperationData("as");
	}
	@Test(expected = BankException.class)
	public void EmptyReference(){
		Bank.getOperationData("");
	}
	@Test(expected = BankException.class)
	public void SpaceReference(){
		Bank.getOperationData(" ");
	}
	
	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}
}
