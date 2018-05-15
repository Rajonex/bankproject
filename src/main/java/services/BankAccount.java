package services;

public class BankAccount extends Service implements Cloneable{

    public BankAccount(int ownerId) {
        super(ownerId);
    }

    public BankAccount(double balance, int ownerId) {
        super(balance, ownerId);
    }

    public Object clone() throws CloneNotSupportedException {
// tutaj: specyficzne operacje związane z klonowaniem
        return super.clone();
    }

}
