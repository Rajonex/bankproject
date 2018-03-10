package services;

import operations.Interest;

public class Deposit extends ConnectedAccount {

    public Deposit(BankAccount bankAccount, int ownerId) {
        super(bankAccount, ownerId);
        canBeNegative = false;
    }

    public Deposit(BankAccount bankAccount, double balance, int ownerId) {
        super(bankAccount, balance, ownerId);
        canBeNegative = false;
    }


}
