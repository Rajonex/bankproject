package services;

public class ConnectedAccount extends Service {
    protected BankAccount bankAccount;

    public ConnectedAccount(BankAccount bankAccount, int ownerId, double percentage)
    {
        super(ownerId, percentage);
        this.bankAccount = bankAccount;
    }

    public ConnectedAccount(BankAccount bankAccount, double balance, int ownerId, double percentage)
    {
        super(balance, ownerId, percentage);
        this.bankAccount = bankAccount;
    }

    /**
     * Getter
     * @return bank account, which is connected with this kind of account
     */
    public BankAccount getBankAccount() {
        return bankAccount;
    }

}
