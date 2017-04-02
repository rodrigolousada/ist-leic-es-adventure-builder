package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.Verifications;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;

public class ReserveActivityStateProcessMethodTest {
		private static final String IBAN = "BK01987654321";
		private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
		private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
		private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
		private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
		private static final String ROOM_CONFIRMATION = "RoomConfirmation";
		private static final String ROOM_CANCELLATION = "RoomCancellation";
		private final LocalDate begin = new LocalDate(2016, 12, 19);
		private final LocalDate end = new LocalDate(2016, 12, 21);
		private Adventure adventure;

		@Injectable
		private Broker broker;

		@Before
		public void setUp() {
			this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
			this.adventure.setState(new ReserveActivityState());
		}

		@Test
		public void Book_Room_Test(@Mocked final ActivityInterface activityInterface) {

			this.adventure.process();

			Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());

			new Verifications() {
				{
					ActivityInterface.reserveActivity(begin,end,this.anyInt);
					this.times = 1;
					
				}
			};
		}
		@Test
		public void Confirmed_Test(@Mocked final ActivityInterface activityInterface) {
			this.adventure = new Adventure(this.broker, this.begin, this.begin, 20, IBAN, 300);
			this.adventure.setState(new ReserveActivityState());
			this.adventure.process();

			Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());

			new Verifications() {
				{
					ActivityInterface.reserveActivity(begin,begin,this.anyInt);
					this.times = 1;
					
				}
			};
		}
		@Test
		public void RemoteAccessException_Test(@Mocked final ActivityInterface activityInterface) {
			
			new StrictExpectations() {
				{
					ActivityInterface.reserveActivity(begin,end,this.anyInt);
					this.result = new RemoteAccessException();
				}
			};

			this.adventure.process();

			Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		}
		@Test
		public void RemoteAccessExceptionToUndo_Test(@Mocked final ActivityInterface activityInterface) {
			new StrictExpectations() {
				{
					ActivityInterface.reserveActivity(begin,end,this.anyInt);
					this.result = new RemoteAccessException();
					ActivityInterface.reserveActivity(begin,end,this.anyInt);
					this.result = new RemoteAccessException();
					ActivityInterface.reserveActivity(begin,end,this.anyInt);
					this.result = new RemoteAccessException();
					ActivityInterface.reserveActivity(begin,end,this.anyInt);
					this.result = new RemoteAccessException();
					ActivityInterface.reserveActivity(begin,end,this.anyInt);
					this.result = new RemoteAccessException();
					
				}
			};

			this.adventure.process();
			this.adventure.process();
			this.adventure.process();
			this.adventure.process();
			this.adventure.process();

			Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		}
		@Test
		public void ActivityException_Test(@Mocked final ActivityInterface activityInterface) {
			
			new StrictExpectations() {
				{
					ActivityInterface.reserveActivity(begin,end,this.anyInt);
					this.result = new ActivityException();
				}
			};

			this.adventure.process();

			Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		}



}
