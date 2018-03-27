package operations;


import messages.Ack;
import messages.TypeOperation;
import services.BankAccount;
import services.Deposit;

import java.time.LocalDate;

//lokata
public class DepositOperation {

    /**
     * Changing the interest system
     *
     * @param deposit       = the deposit
     * @param newPercentage = new interest system
     * @param description   = description of the operation
     * @return true if the percentage is changed
     */
    public static boolean changePercentage(Deposit deposit, double newPercentage, String description) {
        double oldPercentage = deposit.getPercentage();
        // BankAccount bankAccount = deposit.getBankAccount();
        deposit.setPercentage(newPercentage);
        Ack ack = new Ack(deposit, null, TypeOperation.CHANGE_PERCENTAGE, LocalDate.now(),
                "Change percentage from " + oldPercentage * 100 + " to " + newPercentage * 100 + ". " + description);
        deposit.addToHistory(ack);
        return true;
    }

    /**
     * Creating new deposit
     *
     * @param bankAccount = a bank account for which it is created
     * @param value       = the value which is payed to the deposit
     * @param ownerId     = id of the owner of the bank account
     * @param percentage  = interest system
     * @param description = description of the operation
     * @return return the deposit if it is created, null otherwise
     */
    public static Deposit createDeposit(BankAccount bankAccount, double value, int ownerId, double percentage, String description)

    {
        if (bankAccount.decreaseBalance(value)) {
            Deposit deposit = new Deposit(bankAccount, value, ownerId, percentage);

            Ack ack = new Ack(deposit, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
            Ack ackBankAccount = new Ack(bankAccount, deposit, TypeOperation.TRANSFER, LocalDate.now(), description);
            deposit.addToHistory(ack);

            deposit.addToHistory(ackBankAccount);
            bankAccount.addToHistory(ackBankAccount);

            return deposit;
        }
        return null;
    }

    /**
     * Setting deposits value to zero, not destroying object
     * Not adding the percentage  to the value of the deposit
     *
     * @param deposit     = the deposit
     * @param description = description of the operation
     * @return return true if the deposit is set to zero
     */
    public static boolean breakUpDeposit(Deposit deposit, String description) {
        BankAccount bankAccount = deposit.getBankAccount();
        double value = deposit.getBalance();

        if (deposit.decreaseBalance(value)) {
            bankAccount.increaseBalance(value);

            Ack ack = new Ack(deposit, bankAccount, TypeOperation.TRANSFER, LocalDate.now(), description);
            deposit.addToHistory(ack);
            bankAccount.addToHistory(ack);
            return true;
        }

        return false;
    }

    /**
     * Setting deposit value to zero, not destroying the object
     * Adding the percentage to the value of the deposit
     *
     * @param deposit     = the deposit
     * @param description = description of the operation
     * @return return true if the deposit is set to zero
     */
    public static boolean solveDeposit(Deposit deposit, String description) {

        BankAccount bankAccount = deposit.getBankAccount();

        double value = deposit.getBalance();

        if (deposit.decreaseBalance(value)) {
            double newValue = value + value * deposit.getPercentage();

            bankAccount.increaseBalance(newValue);

            Ack ack = new Ack(deposit, bankAccount, TypeOperation.TRANSFER, LocalDate.now(), description);
            deposit.addToHistory(ack);
            bankAccount.addToHistory(ack);

            return true;
        }

        return false;
    }

}
