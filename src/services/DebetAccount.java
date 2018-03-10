package services;

public class DebetAccount extends BankAccount {
    private double limit; // it has minus value and it is max debet value



    public DebetAccount(int ownerId, double limit)
    {
        super(ownerId);
        canBeNegative = true;
        this.limit = limit;
    }

    public DebetAccount(double balance, int ownerId, double limit)
    {
        super(balance, ownerId);
        canBeNegative = true;
        this.limit = limit;
    }

    /**
     * Decreasing balance, it can't be lower than limit which is setting in constructor
     * @param value value subtracted from balance
     * @return feedback of the success of the operation
     */
    @Override
    public boolean decreaseBalance(double value) {
        if(balance - value < limit)
            return false;
        balance -= value;
        return true;
    }

}
