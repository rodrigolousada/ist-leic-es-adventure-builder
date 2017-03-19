package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankGetAccountMethodTest {
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
	public void nullIBAN() {
		this.bank.getAccount(null);
	}

	@Test(expected = BankException.class)
	public void emptyIBAN() {
		this.bank.getAccount("");
	}

	@Test(expected = BankException.class)
	public void blankIBAN() {
		this.bank.getAccount("    ");
	}

	@Test(expected = BankException.class)
	public void emptySetOfAccounts() {
		this.bank.getAccount("XPTO");
	}

	@Test(expected = BankException.class)
	public void severalAccountsDoNoMatch() {
		new Account(this.bank, this.client);
		new Account(this.bank, this.client);

		this.bank.getAccount("XPTO");
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
