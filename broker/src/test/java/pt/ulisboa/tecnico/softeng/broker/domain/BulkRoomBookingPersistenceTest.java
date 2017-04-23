package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.ArrayList;
import java.util.stream.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class BulkRoomBookingPersistenceTest {
	private static final String BROKER_NAME = "Happy Going";
	private static final String BROKER_CODE = "BK1017";

	private static final int BBR_NUMBER = 123;
	private static final LocalDate BBR_ARRIVAL = new LocalDate(2016, 12, 19);
	private static final LocalDate BBR_DEPARTURE = new LocalDate(2016, 12, 21);

	private Set<String> BOOKING_REFERENCES = Stream.of("ref1", "ref2", "ref3").collect(Collectors.toSet());

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Broker broker = new Broker(BROKER_CODE, BROKER_NAME);
		BulkRoomBooking bbr = new BulkRoomBooking(BBR_NUMBER, BBR_ARRIVAL, BBR_DEPARTURE);

		broker.addBulkRoomBooking(bbr);

		bbr.getReferenceSet()
				.addAll(BOOKING_REFERENCES.stream().map(r -> new BookingReference(r)).collect(Collectors.toSet()));
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		Broker broker = new ArrayList<Broker>(FenixFramework.getDomainRoot().getBrokerSet()).get(0);
		assertEquals(1, broker.getBulkRoomBookingSet().size());

		BulkRoomBooking bbr = new ArrayList<BulkRoomBooking>(broker.getBulkRoomBookingSet()).get(0);

		assertEquals(BBR_NUMBER, bbr.getNumber());
		assertEquals(BBR_ARRIVAL, bbr.getArrival());
		assertEquals(BBR_DEPARTURE, bbr.getDeparture());
		assertEquals(BOOKING_REFERENCES.size(), bbr.getReferenceSet().size());

		Set<String> refs = bbr.getReferenceSet().stream().map(r -> r.getReference()).collect(Collectors.toSet());

		assertEquals(refs, BOOKING_REFERENCES);
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			broker.delete();
		}
	}

}
