package reports;

import services.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class filter accounts by date when main account was created, for account type ConnectedAccount it is date of created parent account. For account type BankAccount or DebetAccountDecorator is the date they were created.
 */
public class ReportCreateMainAccountDate implements Report{

    private List<Product> productsWithCriteria;
    private LocalDate dateCriteria;


    public ReportCreateMainAccountDate(LocalDate date)
    {
        productsWithCriteria = new ArrayList<>();
        dateCriteria = date;
    }

    @Override
    public void visit(BankAccount bankAccount) {
        if(bankAccount.getLocalDate().isBefore(dateCriteria))
        {
            productsWithCriteria.add(bankAccount);
        }
    }

    @Override
    public void visit(Credit credit) {
        if(credit.getBankAccount().getLocalDate().isBefore(dateCriteria))
        {
            productsWithCriteria.add(credit);
        }
    }

    @Override
    public void visit(DebetAccountDecorator debetAccountDecorator) {
        if(debetAccountDecorator.getLocalDate().isBefore(dateCriteria))
        {
            productsWithCriteria.add(debetAccountDecorator);
        }
    }

    @Override
    public void visit(Deposit deposit) {
        if(deposit.getBankAccount().getLocalDate().isBefore(dateCriteria))
        {
            productsWithCriteria.add(deposit);
        }
    }

    @Override
    public List<Product> getProductsWithCriteria() {
        return productsWithCriteria;
    }
}
