package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;

public class OperationPersistenceTest {
 private static final String BANK_CODE = "BK01";
 private static final String BANK_NAME = "Money";
 
 private static final String CLIENT_NAME = "John Smith";
 
 private static final Type OPERATION_TYPE_DEPOSIT = Type.DEPOSIT;
 private static final Type OPERATION_TYPE_WITHDRAW = Type.WITHDRAW;
 private static final int DEPOSIT_VALUE = 500;
 private static final int WITHDRAW_VALUE = 250;
 
 private Bank bank_local;
 private Client client_local;
 private Account account_local;

 
 @Test
 public void success() {
  atomicProcess();
  atomicAssert();
 }
 
 @Atomic(mode = TxMode.WRITE)
 public void atomicProcess() {
  this.bank_local = new Bank(BANK_NAME,BANK_CODE);
  this.client_local = new Client (this.bank_local, CLIENT_NAME);
  this.account_local = new Account(bank_local, client_local);
  
  
  account_local.deposit(DEPOSIT_VALUE);
  account_local.withdraw(WITHDRAW_VALUE);
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
  assertEquals(2, bank.getOperationSet().size());
  
  List<Operation> operations = new ArrayList<>(bank.getOperationSet());
  Operation deposit = operations.get(0);
  Operation withdraw = operations.get(1);
  
  assertEquals(account_local, deposit.getAccount());
  assertEquals(DEPOSIT_VALUE, deposit.getValue());
  assertEquals(OPERATION_TYPE_DEPOSIT, deposit.getType());
  
  assertEquals(account_local, withdraw.getAccount());
  assertEquals(WITHDRAW_VALUE, withdraw.getValue());
  assertEquals(OPERATION_TYPE_WITHDRAW, withdraw.getType());
  
  
  assertEquals(DEPOSIT_VALUE - WITHDRAW_VALUE, account_local.getBalance());
 }
 
 @After
 @Atomic(mode = TxMode.WRITE)
 public void tearDown() {
  for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
   bank.delete();
  }
 }
}