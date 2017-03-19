package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;

abstract class AdventureState {
	int numOfRemoteErrors = 0;

	int getNumOfRemoteErrors() {
		return this.numOfRemoteErrors;
	}

	void incNumOfRemoteErrors() {
		this.numOfRemoteErrors++;
	}

	void resetNumOfRemoteErrors() {
		this.numOfRemoteErrors = 0;
	}

	public abstract State getState();

	public abstract void process(Adventure adventure);
}