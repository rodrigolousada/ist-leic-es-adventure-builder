package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Broker extends Broker_Base {
	private static Logger logger = LoggerFactory.getLogger(Broker.class);

	public Broker(String code, String name) {
		checkCode(code);
		setCode(code);

		checkName(name);
		setName(name);

		FenixFramework.getDomainRoot().addBroker(this);
	}

	public void delete() {
		setRoot(null);

		for (Adventure adventure : getAdventureSet()) {
			adventure.delete();
		}

		deleteDomainObject();
	}

	private void checkCode(String code) {
		if (code == null || code.trim().length() == 0) {
			throw new BrokerException();
		}

		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			if (broker.getCode().equals(code)) {
				throw new BrokerException();
			}
		}
	}

	private void checkName(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new BrokerException();
		}
	}

	public void bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		BulkRoomBooking bulkBooking = new BulkRoomBooking(number, arrival, departure);
		this.addBulkRoomBooking(bulkBooking);
		bulkBooking.processBooking();
	}

}
