package operations;

import java.time.LocalDate;

import history.History;
import messages.Ack;
import messages.TypeOperation;
import services.BankAccount;
import services.Credit;
import services.NormalAccount;

public class CreditOperation {

	public static boolean transfer(Credit credit, double value, String description, History bankHistory) {
		boolean flag = false;
		BankAccount bankAccount = credit.getBankAccount();
		if (credit.increaseBalance(value)) {
			if (bankAccount.decreaseBalance(value)) {

				Ack ack = new Ack(credit, bankAccount, TypeOperation.TRANSFER, LocalDate.now(), description);
				credit.addToHistory(ack);
				bankAccount.addToHistory(ack);
				bankHistory.add(ack);

				flag = true;
			} else {
				credit.decreaseBalance(value);
			}
		}

		return flag;

	}

	public static boolean payPercentage(Credit credit, String description, History bankHistory) {
		boolean flag = false;
		BankAccount bankAccount = credit.getBankAccount();
		double balance = credit.getBalance();
		
		if (balance > 0) {
			credit.increaseBalance(balance * credit.getPercentage());
			flag = true;
			
			Ack ack = new Ack(credit, bankAccount, TypeOperation.PAY_PERCENTAGE, LocalDate.now(), description);
			credit.addToHistory(ack);
			bankHistory.add(ack);
		}

		return flag;
	}

	public static boolean changePercentage(Credit credit, double newPercentage, History bankHistory,
			String description) 
	{
		double oldPercentage = credit.getPercentage();
		BankAccount bankAccount = credit.getBankAccount();
		
		credit.setPercentage(newPercentage);
		
		Ack ack = new Ack(credit, bankAccount, TypeOperation.CHANGE_PERCENTAGE, LocalDate.now(),
				"Change percentage from " + oldPercentage * 100 + " to " + newPercentage * 100 + ". " + description);
		credit.addToHistory(ack);
		bankHistory.add(ack);
		return true;
	}
	
	
	

	public static Credit createCredit(BankAccount bankAccount, double balance, int ownerId, double percentage,
			History bankHistory, String description)

	{
		Credit credit = new Credit(bankAccount, balance * (-1), ownerId, percentage);
		Ack ack = new Ack(credit, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
		Ack ackBankAccount = new Ack(credit, bankAccount, TypeOperation.TRANSFER, LocalDate.now(), description);
		credit.addToHistory(ack);
		credit.addToHistory(ackBankAccount);
		bankHistory.add(ack);
		bankHistory.add(ackBankAccount);
		bankAccount.addToHistory(ackBankAccount);
		bankAccount.increaseBalance(balance);
		return credit;
	}

}
