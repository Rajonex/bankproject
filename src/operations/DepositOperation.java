package operations;

import java.time.LocalDate;

import history.History;
import messages.Ack;
import messages.TypeOperation;
import services.BankAccount;
import services.Deposit;

//lokata
public class DepositOperation {

	public static boolean changePercentage(Deposit deposit, double newPercentage, History bankHistory,
			String description) {
		double oldPercentage = deposit.getPercentage();
		// BankAccount bankAccount = deposit.getBankAccount();

		deposit.setPercentage(newPercentage);

		Ack ack = new Ack(deposit, null, TypeOperation.CHANGE_PERCENTAGE, LocalDate.now(),
				"Change percentage from " + oldPercentage * 100 + " to " + newPercentage * 100 + ". " + description);
		deposit.addToHistory(ack);
		bankHistory.add(ack);
		return true;
	}

	public static Deposit createDeposit(BankAccount bankAccount, double value, int ownerId, double percentage,
			History bankHistory, String description)

	{
		if (bankAccount.decreaseBalance(value)) {
			Deposit deposit = new Deposit(bankAccount, value, ownerId, percentage);

			Ack ack = new Ack(deposit, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
			Ack ackBankAccount = new Ack(bankAccount, deposit, TypeOperation.TRANSFER, LocalDate.now(), description);
			deposit.addToHistory(ack);
			bankHistory.add(ack);

			deposit.addToHistory(ackBankAccount);
			bankHistory.add(ackBankAccount);
			bankAccount.addToHistory(ackBankAccount);

			return deposit;
		}
		return null;
	}

	/**
	 * Setting deposits value to zero, not destroying object
	 * 
	 * @param deposit
	 * @param bankAccount
	 * @param description
	 * @param bankHistory
	 * @return
	 */
	public static boolean breakUpDeposit(Deposit deposit, String description, History bankHistory) {
		BankAccount bankAccount = deposit.getBankAccount();
		double value = deposit.getBalance();

		if (deposit.decreaseBalance(value)) {
			bankAccount.increaseBalance(value);

			Ack ack = new Ack(deposit, bankAccount, TypeOperation.TRANSFER, LocalDate.now(), description);
			deposit.addToHistory(ack);
			bankAccount.addToHistory(ack);
			bankHistory.add(ack);
			return true;
		}

		return false;
	}

	public static boolean solveDeposit(Deposit deposit, String description, History bankHistory) {

		BankAccount bankAccount = deposit.getBankAccount();

		double value = deposit.getBalance();

		if (deposit.decreaseBalance(value)) {
			double newValue = value + value * deposit.getPercentage();

			bankAccount.increaseBalance(newValue);

			Ack ack = new Ack(deposit, bankAccount, TypeOperation.TRANSFER, LocalDate.now(), description);
			deposit.addToHistory(ack);
			bankAccount.addToHistory(ack);
			bankHistory.add(ack);

			return true;
		}
		
		return false;
	}

}
