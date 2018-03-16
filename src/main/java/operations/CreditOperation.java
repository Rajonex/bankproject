package main.java.operations;

import main.java.history.History;
import main.java.messages.Ack;
import main.java.messages.TypeOperation;
import main.java.services.BankAccount;
import main.java.services.Credit;

import java.time.LocalDate;

import static com.sun.org.apache.xml.internal.serializer.utils.Utils.messages;

public class CreditOperation {


    /**
     * Paying of the rest of the credit, if the remaining credit is less than the amount of one transfer from the user
     *
     * @param credit      = the credit
     * @param description = the description of the operation
     * @param bankHistory = the history of transaction
     * @return return true if the credit is payed off
     */
    public static boolean payOfCredit(Credit credit, String description, History bankHistory) {
        double remainingValue = credit.getBalance();
        BankAccount bankAccount = credit.getBankAccount();

        if (bankAccount.decreaseBalance(remainingValue)) {
            credit.increaseBalance(remainingValue);

            Ack ack = new Ack(credit, bankAccount, TypeOperation.TRANSFER, LocalDate.now(), description);
            credit.addToHistory(ack);
            bankAccount.addToHistory(ack);
            bankHistory.add(ack);

            return true;
        }
        return false;
    }

    /**
     * Paying of a part of the credit
     *
     * @param credit      = the credit
     * @param value       = value, which the user is trying to send
     * @param description = the description of the operation
     * @param bankHistory = the history of transaction
     * @return return true if the transfer is a success
     */
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
        }// else payOfCredit(credit, description, bankHistory);

        return flag;

    }

    /**
     * Increasing the credit balance with interest
     *
     * @param credit      = the credit
     * @param description = the description
     * @param bankHistory = history of the transaction
     * @return return true if the balance of credit is increased
     */
    public static boolean payPercentage(Credit credit, String description, History bankHistory) {
        boolean flag = false;
        //BankAccount bankAccount = credit.getBankAccount();
        double balance = credit.getBalance();

        if (balance < 0) {
            credit.decreaseBalance(balance * credit.getPercentage());
            flag = true;

            Ack ack = new Ack(credit, null, TypeOperation.PAY_PERCENTAGE, LocalDate.now(), description);
            credit.addToHistory(ack);
            bankHistory.add(ack);
        }

        return flag;
    }

    /**
     * Changing the interest system
     *
     * @param credit        = the credit
     * @param newPercentage = new interest system
     * @param bankHistory   = history of the transaction
     * @param description   = description of the transaction
     * @return return true if the interest system is changed
     */
    public static boolean changePercentage(Credit credit, double newPercentage, History bankHistory,
                                           String description) {
        double oldPercentage = credit.getPercentage();
        //BankAccount bankAccount = credit.getBankAccount();

        credit.setPercentage(newPercentage);

        Ack ack = new Ack(credit, null, TypeOperation.CHANGE_PERCENTAGE, LocalDate.now(),
                "Change percentage from " + oldPercentage * 100 + " to " + newPercentage * 100 + ". " + description);
        credit.addToHistory(ack);
        bankHistory.add(ack);

        return true;
    }


    /**
     * Creating a new credit for a bank account
     *
     * @param bankAccount = bank account which wants to create a credit
     * @param balance     = the amount for the credit
     * @param ownerId     = ID of the owner of the bank account
     * @param percentage  = interest system
     * @param bankHistory = history of the operation
     * @param description = the description of the operation
     * @return return credit
     */
    public static Credit createCredit(BankAccount bankAccount, double balance, int ownerId, double percentage,
                                      History bankHistory, String description)

    {
        Credit credit = new Credit(bankAccount, balance * (-1), ownerId, percentage);

        Ack ack = new Ack(credit, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
        Ack ackBankAccount = new Ack(credit, bankAccount, TypeOperation.TRANSFER, LocalDate.now(), description);

        credit.addToHistory(ack);
        bankHistory.add(ack);


        bankHistory.add(ackBankAccount);
        credit.addToHistory(ackBankAccount);
        bankAccount.addToHistory(ackBankAccount);

        bankAccount.increaseBalance(balance);
        return credit;
    }

}
