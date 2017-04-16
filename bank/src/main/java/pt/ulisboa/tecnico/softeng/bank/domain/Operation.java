package pt.ulisboa.tecnico.softeng.bank.domain;

import org.joda.time.DateTime;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Operation extends Operation_Base {
	public static enum Type {
		DEPOSIT, WITHDRAW
	};

	private static int counter = 0;

	private final String reference;
	private final Type type;
	private final Account account;
	private final int value;
	private final DateTime time;

	public Operation(Type type, Account account, int value) {
		checkArguments(type, account, value);

		this.reference = account.getBank().getCode() + Integer.toString(++Operation.counter);
		this.type = type;
		this.account = account;
		this.value = value;
		this.time = DateTime.now();

		account.getBank().addLog(this);
	}
	
	public void delete() {
		deleteDomainObject();
	}
	
	private void checkArguments(Type type, Account account, int value) {
		if (type == null || account == null || value <= 0) {
			throw new BankException();
		}
	}

	public String getReference() {
		return this.reference;
	}

	public Type getType() {
		return this.type;
	}

	public Account getAccount() {
		return this.account;
	}

	public int getValue() {
		return this.value;
	}

	public DateTime getTime() {
		return this.time;
	}

	public String revert() {
		switch (this.type) {
		case DEPOSIT:
			return this.account.withdraw(this.value);
		case WITHDRAW:
			return this.account.deposit(this.value);
		default:
			throw new BankException();

		}

	}

}
