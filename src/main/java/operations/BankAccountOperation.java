package operations;


import messages.Ack;
import messages.TypeOperation;
import services.BankAccount;
import services.DebetAccount;
import services.NormalAccount;

import java.time.LocalDate;

public class BankAccountOperation {
    /**
     * Payment to a bank account
     *
     * @param bankAccount = the bank account which gets the payment
     * @param value       = value of the payment
     * @param description = description of the operation
     * @return return true if the payment is made
     */
    public static boolean payment(BankAccount bankAccount, double value, String description) {
        bankAccount.increaseBalance(value);
        Ack ack = new Ack(bankAccount, null, TypeOperation.PAYMENT, LocalDate.now(), description);
        return true;
    }

    /**
     * Withdrawing a payment from the bank account
     *
     * @param bankAccount = bank account which makes the payment
     * @param value       = value of the payment
     * @param description = description of the operation
     * @return return true if it is succesful, false otherwise
     */
    public static boolean withdraw(BankAccount bankAccount, double value, String description) {
        if (bankAccount.decreaseBalance(value)) {
            Ack ack = new Ack(null, bankAccount, TypeOperation.WITHDRAWN, LocalDate.now(), description);
            return true;
        }
        return false;
    }

    /**
     * Transfering money from one bank account to the other
     *
     * @param bankAccountFrom account from which it is withdrawn
     * @param bankAccountTo   account to which it is payment
     * @param value           value of transfer
     * @param description     description of transfer
     * @return information about succes or not of transaction
     */
    public static boolean transferFromTo(BankAccount bankAccountFrom, BankAccount bankAccountTo, double value, String description) {
        if (bankAccountFrom.decreaseBalance(value)) {
            bankAccountTo.increaseBalance(value);
            Ack ack = new Ack(bankAccountFrom, bankAccountTo, TypeOperation.TRANSFER, LocalDate.now(), description);
            bankAccountFrom.addToHistory(ack);
            bankAccountTo.addToHistory(ack);
            return true;
        }
        return false;
    }

    /**
     * Increase bank balance by the interest system rate
     *
     * @param bankAccount = bank account which balance is to be increased
     * @param description = description of the operation
     * @return return true if succesful, false otherwise
     */
    public static boolean payPercentage(BankAccount bankAccount, String description) {
        boolean flag = false;
        double balance = bankAccount.getBalance();
        if (balance > 0) {
            bankAccount.increaseBalance(balance * bankAccount.getPercentage());
            flag = true;
            Ack ack = new Ack(bankAccount, null, TypeOperation.PAY_PERCENTAGE, LocalDate.now(), description);
            bankAccount.addToHistory(ack);
        }

        return flag;
    }

    /**
     * Changing the interest system
     *
     * @param bankAccount   = the bank account which wants to change the interest system
     * @param newPercentage = new interest system
     * @param description   = description of the operation
     * @return true if the interest system is changed
     */
    public static boolean changePercentage(BankAccount bankAccount, double newPercentage, String description) {
        double oldPercentage = bankAccount.getPercentage();
        bankAccount.setPercentage(newPercentage);
        Ack ack = new Ack(bankAccount, null, TypeOperation.CHANGE_PERCENTAGE, LocalDate.now(), "Change percentage from " + oldPercentage * 100 + " to " + newPercentage * 100 + ". " + description);
        bankAccount.addToHistory(ack);
        return true;
    }

    /**
     * Creating a new debet account
     *
     * @param ownerId     = ID of the owner which wants to create the account
     * @param limit       = limit for the account
     * @param percentage  = interest system
     * @param description = description of the operation
     * @return created account
     */
    public static DebetAccount createDebetAccount(int ownerId, double limit, double percentage, String description) {
        DebetAccount debetAccount = new DebetAccount(ownerId, limit, percentage);
        Ack ack = new Ack(debetAccount, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
        return debetAccount;
    }

    /**
     * Creating a normal bank account
     *
     * @param ownerId     = id of the owner which wants to create the account
     * @param percentage  = interest system
     * @param description = description of the operation
     * @return return created account
     */
    public static NormalAccount createNormalAccount(int ownerId, double percentage, String description) {
        NormalAccount normalAccount = new NormalAccount(ownerId, percentage);
        Ack ack = new Ack(normalAccount, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
        normalAccount.addToHistory(ack);
        return normalAccount;
    }

    /**
     * Change existing account to a debet account
     *
     * @param bankAccount = bank account to be changed
     * @param limit       = limit of the debet
     * @param description = description of the operation
     * @return return true if the account is created
     */
    public static boolean makeAccountDebet(BankAccount bankAccount, double limit, String description) {
        bankAccount = new DebetAccount(bankAccount, limit);
        Ack ack = new Ack(bankAccount, null, TypeOperation.MAKE_DEBET, LocalDate.now(), description);
        bankAccount.addToHistory(ack);
        return true;
    }

    /**
     * change existing debet account to a normal account
     *
     * @param bankAccount = bank account to be changed
     * @param description = description of the operation
     * @return return true if the account is created
     */
    public static boolean makeAccountNormal(BankAccount bankAccount, String description) {
        if (bankAccount.getBalance() > 0) {
            bankAccount = new NormalAccount(bankAccount);
            Ack ack = new Ack(bankAccount, null, TypeOperation.MAKE_NORMAL, LocalDate.now(), description);
            bankAccount.addToHistory(ack);
            return true;
        }
        return false;
    }
}
