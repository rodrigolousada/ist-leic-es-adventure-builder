package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AccountWithdrawMethodTest {
	private Bank bank;
	private Account account;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		Client client = new Client(this.bank, "António");
		this.account = new Account(this.bank, client);
		this.account.deposit(100);
	}
	
	// Invalid value
	@Test(expected = BankException.class)
	public void invalidValue1() {
		String reference = this.account.withdraw(0);
	}
	@Test(expected = BankException.class)
	public void invalidValue2() {
		String reference = this.account.withdraw(-40);
	}
	@Test(expected = BankException.class)
	public void invalidValue3() {
		String reference = this.account.withdraw(this.account.getBalance()+10);
	}

	@Test
	public void success() {
		String reference = this.account.withdraw(40);

		Assert.assertEquals(60, this.account.getBalance());
		Operation operation = this.bank.getOperation(reference);
		Assert.assertEquals(Operation.Type.WITHDRAW, operation.getType());
		Assert.assertEquals(this.account, operation.getAccount());
		Assert.assertEquals(40, operation.getValue());
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
