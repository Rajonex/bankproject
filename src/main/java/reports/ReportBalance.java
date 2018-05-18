package reports;

import services.*;

import java.util.ArrayList;
import java.util.List;

public class ReportBalance implements Report {

    private List<Product> productsWithCriteria;
    private double criteriaBalance;


    public ReportBalance(double balance)
    {
        productsWithCriteria = new ArrayList<>();
        criteriaBalance = balance;
    }

    @Override
    public void visit(BankAccount bankAccount)
    {
        if(bankAccount.getBalance() > criteriaBalance)
        {
            productsWithCriteria.add(bankAccount);
        }
    }

    @Override
    public void visit(Credit credit)
    {
        if(Math.abs(credit.getBalance()) > criteriaBalance)
        {
            productsWithCriteria.add(credit);
        }
    }

    @Override
    public void visit(DebetAccountDecorator debetAccountDecorator)
    {
        if(debetAccountDecorator.getBalance() > criteriaBalance)
        {
            productsWithCriteria.add(debetAccountDecorator);
        }
    }

    @Override
    public void visit(Deposit deposit)
    {
        if(deposit.getBalance() > criteriaBalance)
        {
            productsWithCriteria.add(deposit);
        }
    }

    @Override
    public List<Product> getProductsWithCriteria() {
        return productsWithCriteria;
    }
}
