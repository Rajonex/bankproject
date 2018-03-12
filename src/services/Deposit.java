package services;


public class Deposit extends ConnectedAccount {

    public Deposit(BankAccount bankAccount, int ownerId, double percentage) {
        super(bankAccount, ownerId, percentage);
        canBeNegative = false;
    }

    public Deposit(BankAccount bankAccount, double balance, int ownerId, double percentage) {
        super(bankAccount, balance, ownerId, percentage);
        canBeNegative = false;
    }


}
