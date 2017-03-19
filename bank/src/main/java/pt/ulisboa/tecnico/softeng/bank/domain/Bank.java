package pt.ulisboa.tecnico.softeng.bank.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Bank {
	public static Set<Bank> banks = new HashSet<>();

	public static final int CODE_SIZE = 4;

	private final String name;
	private final String code;
	private final Set<Account> accounts = new HashSet<>();
	private final Set<Client> clients = new HashSet<>();
	private final List<Operation> log = new ArrayList<>();

	public Bank(String name, String code) {
		if (name == null || code == null) {
			throw new BankException("null argument");
		}

		checkCode(code);
		checkName(name);

		this.name = name;
		this.code = code;

		if (!Bank.banks.add(this)) {
			throw new BankException("Duplicate bank code: " + code);
		}

	}

	private void checkCode(String code) {
		if (code.trim().length() == 0) {
			throw new BankException("Invalid bank code (only whitespace)");
		}
		if (code.length() != Bank.CODE_SIZE) {
			throw new BankException("Invalid bank code length (must be " + Bank.CODE_SIZE + " characters long)");
		}
	}

	private void checkName(String name) {
		if (name.trim().length() == 0) {
			throw new BankException("No bank name");
		}
	}

	String getName() {
		return this.name;
	}

	String getCode() {
		return this.code;
	}

	int getNumberOfAccounts() {
		return this.accounts.size();
	}

	int getNumberOfClients() {
		return this.clients.size();
	}

	void addAccount(Account account) {
		this.accounts.add(account);
	}

	boolean hasClient(Client client) {
		return this.clients.contains(client);
	}

	void addClient(Client client) {
		this.clients.add(client);
	}

	void addLog(Operation operation) {
		this.log.add(operation);
	}

	public Account getAccount(String IBAN) {
		if (IBAN == null) {
			throw new BankException("null argument");
		}
		if (this.accounts.isEmpty()) {
			throw new BankException("there are no accounts yet");
		}
		for (Account account : this.accounts) {
			if (account.getIBAN().equals(IBAN)) {
				return account;
			}
		}
		throw new BankException("account not found: " + IBAN);
	}

	public Operation getOperation(String reference) {
		for (Operation operation : this.log) {
			if (operation.getReference().equals(reference)) {
				return operation;
			}
		}
		return null;
	}

	public static String processPayment(String IBAN, int amount) {
		for (Bank bank : Bank.banks) {
			if (bank.getAccount(IBAN) != null) {
				return bank.getAccount(IBAN).withdraw(amount);
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		return this.code.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!Bank.class.isAssignableFrom(obj.getClass())) {
			return false;
		}

		final Bank other = (Bank) obj;

		// Banks are considered equal if they have the same code
		return this.code.equals(other.code);
	}

}
