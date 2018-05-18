package services;

import interests.InterestsMechanism;
import messages.Ack;
import reports.Report;
import reports.ReportBalance;

import java.time.LocalDate;
import java.util.List;

public interface Product /*extends Cloneable*/ {
    /**
     * Increasing balance
     *
     * @param value value added to balance
     * @return feedback of the success of the operation
     */
    public boolean increaseBalance(double value);

    /**
     * Decreasing balance, considering kind of account
     *
     * @param value value subtracted from balance
     * @return feedback of the success of the operation
     */
    public boolean decreaseBalance(double value);

    /**
     * Getter
     *
     * @return owner id of this service
     */
    public int getOwnerId();

    /**
     * Getter
     *
     * @return id object of this service
     */
    public int getId();

    public InterestsMechanism getInterestsMechanism();

    public void setInterestsMechanism(InterestsMechanism interestsMechanism);

    public double getInterests();

    public double getBalance();

    public LocalDate getLocalDate();

    public boolean addToHistory(Ack ack);

    public List<Ack> showHistory();

    public Object clone() throws CloneNotSupportedException;

    public void accept(Report report);

}