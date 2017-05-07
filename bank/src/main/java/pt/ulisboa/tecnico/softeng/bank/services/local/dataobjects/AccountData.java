package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;

public class AccountData {
	private String iban;
	private int balance;

	public AccountData() {
	}

	public AccountData(Account account) {
		this.iban = account.getIBAN();
		this.balance = account.getBalance();
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public int getBalance() {
		return this.balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
}