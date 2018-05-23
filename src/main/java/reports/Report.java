package reports;

import services.*;

import java.util.List;

public interface Report {
    void visit(BankAccount bankAccount);
    void visit(Credit credit);
    void visit(DebetAccountDecorator debetAccountDecorator);
    void visit(Deposit deposit);
    List<Product> getProductsWithCriteria();
}
