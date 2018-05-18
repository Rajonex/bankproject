package reports;

import services.*;

import java.util.List;

public interface Report {
    public void visit(BankAccount bankAccount);
    public void visit(Credit credit);
    public void visit(DebetAccountDecorator debetAccountDecorator);
    public void visit(Deposit deposit);
    public List<Product> getProductsWithCriteria();
}
