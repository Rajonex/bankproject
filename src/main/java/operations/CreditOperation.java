package operations;


import interests.InterestsMechanism;
import messages.Ack;
import messages.TypeOperation;
import services.BankAccount;
import services.Credit;

import java.time.LocalDate;

public class CreditOperation {


    /**
     * Paying of the rest of the credit, if the remaining credit is less than the amount of one transfer from the user
     *
     * @param credit      = the credit
     * @param description = the description of the operation
     * @return return true if the credit is payed off
     */
    public static boolean payOfCredit(Credit credit, String description) {
        double remainingValue = credit.getBalance();
        BankAccount bankAccount = credit.getBankAccount();

        if (bankAccount.decreaseBalance(remainingValue)) {
            credit.increaseBalance(remainingValue);

            Ack ack = new Ack(credit, bankAccount, TypeOperation.TRANSFER, LocalDate.now(), description);
            credit.addToHistory(ack);
            bankAccount.addToHistory(ack);

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
     * @return return true if the transfer is a success
     */
    public static boolean transfer(Credit credit, double value, String description) {
        boolean flag = false;
        BankAccount bankAccount = credit.getBankAccount();

        if (credit.increaseBalance(value)) {
            if (bankAccount.decreaseBalance(value)) {

                Ack ack = new Ack(credit, bankAccount, TypeOperation.TRANSFER, LocalDate.now(), description);
                credit.addToHistory(ack);
                bankAccount.addToHistory(ack);

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
     * @return return true if the balance of credit is increased
     */
    public static boolean payPercentage(Credit credit, String description) {
        boolean flag = false;
        //BankAccount bankAccount = credit.getBankAccount();
        double balance = credit.getBalance();

        if (balance < 0) {
            credit.decreaseBalance(credit.getInterests());
            flag = true;

            Ack ack = new Ack(credit, null, TypeOperation.PAY_PERCENTAGE, LocalDate.now(), description);
            credit.addToHistory(ack);
        }

        return flag;
    }

    /**
     * Changing the interest system
     *
     * @param credit        = the credit
     *
     * @param description   = description of the transaction
     * @return return true if the interest system is changed
     */
    public static boolean changePercentage(Credit credit, InterestsMechanism mechanism, String description) {

        InterestsMechanism oldMechanism = credit.getInterestsMechanism();
        credit.setInterestsMechanism(mechanism);


        Ack ack = new Ack(credit, null, TypeOperation.CHANGE_PERCENTAGE, LocalDate.now(),
                "Change percentage from " + oldMechanism + " to " + mechanism + ". " + description);
        credit.addToHistory(ack);

        return true;
    }


    /**
     * Creating a new credit for a bank account
     *
     * @param bankAccount = bank account which wants to create a credit
     * @param balance     = the amount for the credit
     * @param ownerId     = ID of the owner of the bank account
     * @param description = the description of the operation
     * @return return credit
     */
    public static Credit createCredit(BankAccount bankAccount, double balance, int ownerId, String description)

    {
        Credit credit = new Credit(bankAccount, balance * (-1), ownerId);

        Ack ack = new Ack(credit, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
        Ack ackBankAccount = new Ack(credit, bankAccount, TypeOperation.TRANSFER, LocalDate.now(), description);

        credit.addToHistory(ack);
        credit.addToHistory(ackBankAccount);

        bankAccount.increaseBalance(balance);
        return credit;
    }

}
