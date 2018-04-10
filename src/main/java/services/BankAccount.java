package services;

public class BankAccount extends Service {

    public BankAccount(int ownerId) {
        super(ownerId);
    }

    public BankAccount(double balance, int ownerId) {
        super(balance, ownerId);
    }
}
