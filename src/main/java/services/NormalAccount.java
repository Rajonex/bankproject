package services;

public class NormalAccount extends BankAccount {

    public NormalAccount(int ownerId) {
        super(ownerId);
    }

    public NormalAccount(double balance, int ownerId) {
        super(balance, ownerId);
    }

    public NormalAccount(BankAccount bankAccount) {
        super(bankAccount.balance, bankAccount.ownerId);
        canBeNegative = true;
        history = bankAccount.history;
    }
}
