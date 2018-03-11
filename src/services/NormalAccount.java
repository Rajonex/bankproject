package services;

public class NormalAccount extends BankAccount {

    public NormalAccount(int ownerId, double percentage)
    {
        super(ownerId, percentage);
    }

    public NormalAccount(double balance, int ownerId, double percentage)
    {
        super(balance, ownerId, percentage);
    }
}
