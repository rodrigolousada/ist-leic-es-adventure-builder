package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class AccountPersistenceTest {
 private static final String BANK_CODE = "BK01";
 private static final String BANK_NAME = "Money";
 
 private static final String CLIENT_NAME = "John Smith";
 
 private Bank bank_local;
 private Client client_local;

 
 @Test
 public void success() {
  atomicProcess();
  atomicAssert();
 }
 
 @Atomic(mode = TxMode.WRITE)
 public void atomicProcess() {
  this.bank_local = new Bank(BANK_NAME,BANK_CODE);
  this.client_local = new Client (this.bank_local, CLIENT_NAME);
  
  new Account(bank_local, client_local);
 }
 
 @Atomic(mode = TxMode.READ)
 public void atomicAssert() {
  assertEquals(1, FenixFramework.getDomainRoot().getBankSet().size());
  
  List<Bank> banks = new ArrayList<>(FenixFramework.getDomainRoot().getBankSet());
  Bank bank = banks.get(0);

  assertEquals(BANK_NAME, bank.getName());
  assertEquals(BANK_CODE, bank.getCode());
  assertEquals(1, bank.getClientSet().size());
  assertEquals(1, bank.getAccountSet().size());
  assertEquals(0, bank.getOperationSet().size());
  
  List<Client> clients = new ArrayList<>(bank.getClientSet());
  Client client = clients.get(0);
  
  assertNotNull(client.getID());
  assertEquals(CLIENT_NAME, client.getName());
  assertEquals(bank,client.getBank());
  assertEquals(1,client.getAccountSet().size());
  
 List<Account> accounts = new ArrayList<>(client.getAccountSet());
  Account account = accounts.get(0);
  
  assertEquals(0,account.getBalance());
  assertNotNull(account.getIBAN());
  assertEquals(bank_local,account.getBank());
  assertEquals(client_local,account.getClient());
  assertEquals(bank_local,bank);
  assertEquals(client_local,client);
  
 }
 
 @After
 @Atomic(mode = TxMode.WRITE)
 public void tearDown() {
  for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
   bank.delete();
  }
 }
}