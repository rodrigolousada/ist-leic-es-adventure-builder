package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Client {
	private static int counter = 0;

	private final String name;
	private final String ID;

	public Client(Bank bank, String name) {
		checkArguments(bank, name);

		this.ID = Integer.toString(++Client.counter);
		this.name = name;

		bank.addClient(this);
	}

	private void checkArguments(Bank bank, String name) {
		if (bank == null || name == null || name.trim().equals("")) {
			throw new BankException();
		}
	}

	public String getName() {
		return this.name;
	}

	public String getID() {
		return this.ID;
	}

}
