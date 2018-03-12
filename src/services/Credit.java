package services;


public class Credit extends ConnectedAccount {

    public Credit(BankAccount bankAccount, int ownerId, double percentage)
    {
        super(bankAccount, ownerId, percentage);
        canBeNegative = true;
    }

    public Credit(BankAccount bankAccount, double balance, int ownerId, double percentage)
    {
        super(bankAccount, balance, ownerId, percentage);
        canBeNegative = true;
    }

    /**
     * Increasing balance
     * @param value value added to balance
     * @return feedback of the success of the operation
     */
    @Override
    public boolean increaseBalance(double value) {
        if(balance+value > 0)
            return false;
        balance += value;
        return true;
    }
}
