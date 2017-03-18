package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientContructorMethodTest {
	Bank bank;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
	}
	
	// Invalid Bank
	@Test(expected = BankException.class)
	public void invalidBank1() {
		Client client = new Client(null, "António");
	}
	
	// Invalid Client
	@Test(expected = BankException.class)
	public void invalidClient1() {
		Client client = new Client(this.bank, null);
	}
	@Test(expected = BankException.class)
	public void invalidClient2() {
		Client client = new Client(this.bank, "");
	}
	@Test(expected = BankException.class)
	public void invalidClient3() {
		Client client = new Client(this.bank, "\t\n  ");
	}
	
	@Test
	public void success() {
		Client client = new Client(this.bank, "António");

		Assert.assertEquals("António", client.getName());
		Assert.assertTrue(client.getID().length() >= 1);
		Assert.assertTrue(this.bank.hasClient(client));
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
