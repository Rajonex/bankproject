package services;

public abstract class ConnectedAccount extends Service {
    protected Product bankAccount;

    public ConnectedAccount(Product bankAccount, int ownerId) {
        super(ownerId);
        this.bankAccount = bankAccount;
    }

    public ConnectedAccount(Product bankAccount, double balance, int ownerId) {
        super(balance, ownerId);
        this.bankAccount = bankAccount;
    }

    /**
     * Getter
     *
     * @return bank account, which is connected with this kind of account
     */
    public Product getBankAccount() {
        return bankAccount;
    }

}