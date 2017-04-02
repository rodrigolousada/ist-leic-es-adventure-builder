package pt.ulisboa.tecnico.softeng.activity.dataobjects;

import org.joda.time.LocalDate;

public class ActivityReservationData {
	private String reference;
	private String cancellation;
	private String name;
	private String code;
	private LocalDate begin;
	private LocalDate end;
	private LocalDate cancellationDate;

	public ActivityReservationData(String reference,
								   String cancellation,
								   String name,
								   String code,
								   LocalDate begin,
								   LocalDate end,
								   LocalDate cancellationDate) {
		this.setReference(reference);
		this.setCancellation(cancellation);
		this.setName(name);
		this.setCode(code);
		this.setBegin(begin);
		this.setEnd(end);
		this.setCancellationDate(cancellationDate);
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getCancellation() {
		return this.cancellation;
	}

	public void setCancellation(String cancellation) {
		this.cancellation = cancellation;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}

	public void setCancellationDate(LocalDate cancellationDate) {
		this.cancellationDate = cancellationDate;
	}
}
