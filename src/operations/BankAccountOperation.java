package operations;

import history.History;
import messages.Ack;
import messages.TypeOperation;
import services.BankAccount;
import services.DebetAccount;
import services.NormalAccount;

import java.time.LocalDate;

public class BankAccountOperation{
    /**
     *
     * @param bankAccount
     * @param value
     * @param description
     * @param bankHistory
     * @return
     */
    public static boolean payment(BankAccount bankAccount, double value, String description, History bankHistory)
    {
        bankAccount.increaseBalance(value);
        Ack ack = new Ack(bankAccount, null, TypeOperation.PAYMENT, LocalDate.now(), description);
        bankAccount.addToHistory(ack);
        bankHistory.add(ack);
        return true;
    }

    /**
     *
     * @param bankAccount
     * @param value
     * @param description
     * @param bankHistory
     * @return
     */
    public static boolean withdraw(BankAccount bankAccount, double value, String description, History bankHistory)
    {
        if(bankAccount.decreaseBalance(value))
        {
            Ack ack = new Ack(bankAccount, null, TypeOperation.WITHDRAWN, LocalDate.now(), description);
            return true;
        }
        return false;
    }

    /**
     *
     * @param bankAccountFrom account from which it is withdrawn
     * @param bankAccountTo account to which it is payment
     * @param value value of transfer
     * @param description description of transfer
     * @return information about succes or not of transaction
     */
    public static boolean transferFromTo(BankAccount bankAccountFrom, BankAccount bankAccountTo, double value, String description, History bankHistory)
    {
        if(bankAccountFrom.decreaseBalance(value))
        {
            bankAccountTo.increaseBalance(value);
            Ack ack = new Ack(bankAccountFrom, bankAccountTo, TypeOperation.TRANSFER, LocalDate.now(), description);
            bankAccountFrom.addToHistory(ack);
            bankAccountTo.addToHistory(ack);
            bankHistory.add(ack);
            return true;
        }
        return false;
    }

    /**
     *
     * @param bankAccount
     * @return true if state of account is positive, if account is on debet it returns false
     */
    public static boolean payPercentage(BankAccount bankAccount, String description, History bankHistory)
    {
        boolean flag = false;
        double balance = bankAccount.getBalance();
        if(balance > 0)
        {
            bankAccount.increaseBalance(balance*bankAccount.getPercentage());
            flag = true;
            Ack ack = new Ack(bankAccount, null, TypeOperation.PAY_PERCENTAGE, LocalDate.now(), description);
            bankAccount.addToHistory(ack);
            bankHistory.add(ack);
        }

        return flag;
    }

    /**
     *
     * @param bankAccount
     * @param newPercentage
     * @param bankHistory
     * @param description
     * @return
     */
    public static boolean changePercentage(BankAccount bankAccount, double newPercentage, History bankHistory, String description)
    {
        double oldPercentage = bankAccount.getPercentage();
        bankAccount.setPercentage(newPercentage);
        Ack ack = new Ack(bankAccount, null, TypeOperation.CHANGE_PERCENTAGE, LocalDate.now(), "Change percentage from " + oldPercentage*100 + " to " + newPercentage*100 + ". " + description);
        bankAccount.addToHistory(ack);
        bankHistory.add(ack);
        return true;
    }

    /**
     *
     * @param ownerId
     * @param limit
     * @param percentage
     * @param bankHistory
     * @param description
     * @return
     */
    public static DebetAccount createDebetAccount(int ownerId, double limit, double percentage, History bankHistory, String description)
    {
        DebetAccount debetAccount = new DebetAccount(ownerId, limit, percentage);
        Ack ack = new Ack(debetAccount, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
        debetAccount.addToHistory(ack);
        bankHistory.add(ack);
        return debetAccount;
    }

    /**
     *
     * @param ownerId
     * @param percentage
     * @param bankHistory
     * @param description
     * @return
     */
    public static NormalAccount createNormalAccount(int ownerId, double percentage, History bankHistory, String description)
    {
        NormalAccount normalAccount = new NormalAccount(ownerId, percentage);
        Ack ack = new Ack(normalAccount, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
        normalAccount.addToHistory(ack);
        bankHistory.add(ack);
        return normalAccount;
    }

    /**
     *
     * @param bankAccount
     * @param limit
     * @param bankHistory
     * @param description
     * @return
     */
    public static boolean makeAccountDebet(BankAccount bankAccount, double limit, History bankHistory, String description)
    {
        bankAccount = new DebetAccount(bankAccount, limit);
        Ack ack = new Ack(bankAccount, null, TypeOperation.MAKE_DEBET, LocalDate.now(), description);
        bankAccount.addToHistory(ack);
        bankHistory.add(ack);
        return true;
    }

    /**
     *
     * @param bankAccount
     * @param bankHistory
     * @param description
     * @return
     */
    public static boolean makeAccountNormal(BankAccount bankAccount, History bankHistory, String description)
    {
        bankAccount = new NormalAccount(bankAccount);
        Ack ack = new Ack(bankAccount, null, TypeOperation.MAKE_NORMAL, LocalDate.now(), description);
        bankAccount.addToHistory(ack);
        bankHistory.add(ack);
        return true;
    }
}
