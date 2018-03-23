package services;


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

    public Deposit(BankAccount bankAccount, int ownerId, double percentage) {
        super(bankAccount, ownerId, percentage);
        canBeNegative = false;
    }

    public Deposit(BankAccount bankAccount, double balance, int ownerId, double percentage) {
        super(bankAccount, balance, ownerId, percentage);
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
}
