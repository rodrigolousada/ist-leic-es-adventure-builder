package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class BulkRoomBookingProcessBookingMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Adventure adventure;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
	}

	@Test
	public void simpleSuccess(@Mocked final HotelInterface hotel) {
		int reservations = 5;
		Set<String> references = new HashSet<>(Arrays.asList("one", "two", "three", "four", "five"));
		LocalDate arrival = this.arrival;
		LocalDate departure = this.departure;

		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = references;
			}
		};
		BulkRoomBooking brb = new BulkRoomBooking(reservations, this.arrival, this.departure);
		Assert.assertTrue(brb.processBooking());
		Assert.assertTrue(brb.isDone());
		Assert.assertFalse(brb.isCancelled());
		Assert.assertEquals(references, brb.getReferences());
	}

	@Test
	public void successAfterExceptions(@Mocked final HotelInterface hotel) {
		int reservations = 5;
		Set<String> references = new HashSet<>(Arrays.asList("one", "two", "three", "four", "five"));
		LocalDate arrival = this.arrival;
		LocalDate departure = this.departure;

		new StrictExpectations() {
			{
				// ERROR - CAN BE NOT WITHOUT REPETIONS
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = references;
			}
		};
		BulkRoomBooking brb = new BulkRoomBooking(reservations, this.arrival, this.departure);
		for (int i = 0; i < 12; i++) {
			Assert.assertFalse(brb.processBooking());
		}
		Assert.assertTrue(brb.processBooking());
		Assert.assertTrue(brb.isDone());
		Assert.assertFalse(brb.isCancelled());
		Assert.assertEquals(references, brb.getReferences());
	}

	@Test
	public void cancelledDueToRemoteAccessExceptions(@Mocked final HotelInterface hotel) {
		int reservations = 5;
		LocalDate arrival = this.arrival;
		LocalDate departure = this.departure;

		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
			}
		};
		BulkRoomBooking brb = new BulkRoomBooking(reservations, this.arrival, this.departure);
		for (int i = 0; i < 8; i++) {
			Assert.assertFalse(brb.processBooking());
		}
		try {
			brb.processBooking();
			Assert.fail();
		} catch (BrokerException e) {

		}
		Assert.assertFalse(brb.isDone());
		Assert.assertTrue(brb.isCancelled());
		Assert.assertEquals(new HashSet<String>(), brb.getReferences());
	}

	@Test
	public void cancelledDueToHotelExceptions(@Mocked final HotelInterface hotel) {
		int reservations = 5;
		LocalDate arrival = this.arrival;
		LocalDate departure = this.departure;

		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new RemoteAccessException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
				HotelInterface.bulkBooking(reservations, arrival, departure);
				this.result = new HotelException();
			}
		};
		BulkRoomBooking brb = new BulkRoomBooking(reservations, this.arrival, this.departure);
		for (int i = 0; i < 8; i++) {
			Assert.assertFalse(brb.processBooking());
		}
		try {
			brb.processBooking();
			Assert.fail();
		} catch (BrokerException e) {

		}
		Assert.assertFalse(brb.isDone());
		Assert.assertTrue(brb.isCancelled());
		Assert.assertEquals(new HashSet<String>(), brb.getReferences());
	}

}
