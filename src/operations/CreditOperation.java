package operations;

public class CreditOperation extends BankOperation implements Interest {
    @Override
    public boolean generateInterests(double interest) {
        return false;
    }
}
