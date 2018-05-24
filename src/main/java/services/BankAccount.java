package services;

import interests.InterestsMechanism;
import reports.Report;

public class BankAccount extends Service implements Cloneable{

    public BankAccount(int ownerId) {
        super(ownerId);
    }

    public BankAccount(double balance, int ownerId, InterestsMechanism interestsMechanism) {
        super(balance, ownerId, interestsMechanism);
    }


    @Override
    public void accept(Report report)
    {
        report.visit(this);
    }

}