package services;

public abstract class ConnectedAccount extends Service {
    protected BankAccount bankAccount;

    public ConnectedAccount(BankAccount bankAccount, int ownerId) {
        super(ownerId);
        this.bankAccount = bankAccount;
    }

    public ConnectedAccount(BankAccount bankAccount, double balance, int ownerId) {
        super(balance, ownerId);
        this.bankAccount = bankAccount;
    }

    /**
     * Getter
     *
     * @return bank account, which is connected with this kind of account
     */
    public BankAccount getBankAccount() {
        return bankAccount;
    }

}
