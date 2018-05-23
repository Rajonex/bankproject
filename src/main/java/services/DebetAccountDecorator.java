package services;


import interests.InterestsMechanism;
import messages.Ack;
import reports.Report;

import java.time.LocalDate;
import java.util.List;

public class DebetAccountDecorator implements Product, Cloneable
{
    private double limit; // it has minus value and it is max debet value
    private double debet;
    private Product bankAccount;


    public DebetAccountDecorator(double limit, double debet, Product bankAccount){
        this.limit = limit;
        this.debet = debet;
        this.bankAccount = bankAccount;
//        try {
//            this.bankAccount = (Product) bankAccount.clone();
//        } catch(CloneNotSupportedException er)
//        {
//            er.printStackTrace();
//        }
    }

    @Override
    public boolean increaseBalance(double value) {
        if(debet > 0)
        {
            if(debet > value)
            {
                debet -= value;
                return true;
            }
            else
            {
                value -= debet;
                return bankAccount.increaseBalance(value);
            }
        }
        else
        {
            return bankAccount.increaseBalance(value);
        }
    }


    @Override
    public boolean decreaseBalance(double value) {
        double balance = bankAccount.getBalance();
        if(balance + limit - debet >= value)
        {
            if(balance >= value)
            {
                return bankAccount.decreaseBalance(value);
            }
            else
            {
                if(balance > 0) {
                    bankAccount.decreaseBalance(balance);
                    value -= balance;
                }
                debet += value;
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public int getOwnerId() {
        return bankAccount.getOwnerId();
    }

    @Override
    public int getId() {
        return bankAccount.getId();
    }

    @Override
    public InterestsMechanism getInterestsMechanism() {
        return bankAccount.getInterestsMechanism();
    }

    @Override
    public void setInterestsMechanism(InterestsMechanism interestsMechanism) {
        bankAccount.setInterestsMechanism(interestsMechanism);
    }

    @Override
    public double getInterests() {
        return bankAccount.getInterests();
    }

    @Override
    public double getBalance() {
        return bankAccount.getBalance() - debet;
    }

    @Override
    public LocalDate getLocalDate() {
        return bankAccount.getLocalDate();
    }

    @Override
    public boolean addToHistory(Ack ack) {
        return bankAccount.addToHistory(ack);
    }

    @Override
    public List<Ack> showHistory() {
        return bankAccount.showHistory();
    }

    @Override
    public void accept(Report report)
    {
        report.visit(this);
    }
}