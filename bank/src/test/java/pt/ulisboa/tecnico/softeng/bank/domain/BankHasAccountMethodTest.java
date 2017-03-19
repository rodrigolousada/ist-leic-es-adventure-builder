package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankHasAccountMethodTest {
	Bank bank;
	Client client;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		this.client = new Client(this.bank, "António");
	}

	@Test
	public void success() {
		Account account = new Account(this.bank, this.client);

		Account result = this.bank.getAccount(account.getIBAN());

		Assert.assertEquals(account, result);
	}

	@Test(expected = BankException.class)
	public void nullArgument() throws Exception {
		new Account(this.bank, this.client);

		this.bank.getAccount(null);
	}

	@Test(expected = BankException.class)
	public void noAccounts() throws Exception {
		this.bank.getAccount("BK01" + 1);
	}

	@Test(expected = BankException.class)
	public void inexistentAccount() throws Exception {
		new Account(this.bank, this.client);
		new Account(this.bank, this.client);
		new Account(this.bank, this.client);

		this.bank.getAccount("BK02" + 1);
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
