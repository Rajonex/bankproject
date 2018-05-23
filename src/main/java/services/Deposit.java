package services;


import reports.Report;

import java.time.LocalDate;

public class Deposit extends ConnectedAccount {
    private long duration; //  in months

    /**
     * Getter
     *
     * @return duration of deposit in months
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Setter
     *
     * @param duration duration of deposit in months
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Deposit(Product bankAccount, int ownerId) {
        super(bankAccount, ownerId);
        canBeNegative = false;
    }

    public Deposit(Product bankAccount, double balance, int ownerId) {
        super(bankAccount, balance, ownerId);
        canBeNegative = false;
    }

    /**
     * checking if deposit has already expired
     *
     * @return true if deposit has expired
     */
    public boolean isExpired() {
        return getLocalDate().plusMonths(getDuration()).isBefore(LocalDate.now());
    }

    @Override
    public void accept(Report report)
    {
        report.visit(this);
    }
}