package pt.ulisboa.tecnico.softeng.bank.domain;

import org.joda.time.DateTime;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Operation extends Operation_Base {
	public static enum Type {
		DEPOSIT, WITHDRAW
	};

	private static int counter = 0;

	private final Type type;
	private final Account account;

	public Operation(Type type, Account account, int value) {
		checkArguments(type, account, value);

		this.setReference(account.getBank().getCode() + Integer.toString(++Operation.counter));
		this.type = type;
		this.account = account;
		this.setValue(value);
		this.setTime(DateTime.now());

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


	public Type getType() {
		return this.type;
	}

	public Account getAccount() {
		return this.account;
	}


	public String revert() {
		switch (this.type) {
		case DEPOSIT:
			return this.account.withdraw(getValue());
		case WITHDRAW:
			return this.account.deposit(getValue());
		default:
			throw new BankException();

		}

	}

}
