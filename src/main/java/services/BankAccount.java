package services;

public class BankAccount extends Service {

    public BankAccount(int ownerId, double percentage)
    {
        super(ownerId, percentage);
    }

    public BankAccount(double balance, int ownerId, double percentage)
    {
        super(balance, ownerId, percentage);
    }
}
