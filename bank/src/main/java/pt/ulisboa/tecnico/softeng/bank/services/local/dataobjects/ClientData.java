package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.bank.domain.Client;

public class ClientData {
	public static enum CopyDepth {
		SHALLOW, ACCOUNTS
	};
	
	private String name;
	private String ID;
	//private List<AccountData> accounts = new ArrayList<>();

	public ClientData() {
	}

	public ClientData(Client client, CopyDepth depth) {
		this.ID = client.getID();
		this.name = client.getName();
		
		switch (depth) {
		/*case ACCOUNTS:
			for (Account account : client.getAccountSet()) {
				this.accounts.add(new AccountData(account));
			}
			break;*/
		}
	}

	public String getID() {
		return this.ID;
	}

	public void setID(String id) {
		this.ID = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}