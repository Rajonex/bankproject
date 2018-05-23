package services;

import reports.Report;
import reports.ReportBalance;

public class BankAccount extends Service implements Cloneable{

    public BankAccount(int ownerId) {
        super(ownerId);
    }

    public BankAccount(double balance, int ownerId) {
        super(balance, ownerId);
    }


    @Override
    public void accept(Report report)
    {
        report.visit(this);
    }

}