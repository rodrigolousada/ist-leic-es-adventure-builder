package pt.ulisboa.tecnico.softeng.bank.domain;

import org.joda.time.LocalDateTime;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Operation {
	public static enum Type {
		DEPOSIT, WITHDRAW
	};

	private static int counter = 0;

	private final String reference;
	private final Type type;
	private final Account account;
	private final int value;
	private final LocalDateTime time;

	public Operation(Type type, Account account, int value) {
		checkArguments(type, account, value);

		this.reference = account.getBank().getCode() + Integer.toString(++Operation.counter);
		this.type = type;
		this.account = account;
		this.value = value;
		this.time = LocalDateTime.now();

		account.getBank().addLog(this);
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

	public LocalDateTime getTime() {
		return this.time;
	}

}
