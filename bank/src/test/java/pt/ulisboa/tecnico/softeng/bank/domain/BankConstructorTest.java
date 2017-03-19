package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankConstructorTest {

	@Before
	public void setUp() {

	}

	// Invalid Bank names
	@Test(expected = BankException.class)
	public void invalidName1() {
		Bank bank = new Bank(null, "BK01");
	}

	@Test(expected = BankException.class)
	public void invalidName2() {
		Bank bank = new Bank("", "BK01");
	}

	@Test(expected = BankException.class)
	public void invalidName3() {
		Bank bank = new Bank("\t\n  ", "BK01");
	}

	// Invalid Bank codes
	@Test(expected = BankException.class)
	public void invalidCode1() {
		Bank bank = new Bank("Money", null);
	}

	@Test(expected = BankException.class)
	public void invalidCode2() {
		Bank bank = new Bank("Money", "    ");
	}

	@Test(expected = BankException.class)
	public void invalidCode3() {
		Bank bank = new Bank("Money", "");
	}

	@Test(expected = BankException.class)
	public void invalidCode4() {
		Bank bank = new Bank("Money", "BK026");
	}

	@Test(expected = BankException.class)
	public void duplicateCode() {
		Bank bank1 = new Bank("Money1", "BK01");
		Bank bank2 = new Bank("Money2", "BK01");
	}

	@Test
	public void success() {
		Bank bank = new Bank("Money", "BK01");

		Assert.assertEquals("Money", bank.getName());
		Assert.assertEquals("BK01", bank.getCode());
		Assert.assertEquals(1, Bank.banks.size());
		Assert.assertEquals(0, bank.getNumberOfAccounts());
		Assert.assertEquals(0, bank.getNumberOfClients());
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}
}
