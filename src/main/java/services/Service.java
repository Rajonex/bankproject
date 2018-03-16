package main.java.services;

import main.java.history.History;
import main.java.messages.Ack;

import java.time.LocalDate;
import java.util.List;

public abstract class Service implements Product {
    protected History history; // history of this service
    private int id; // id this service object
    protected boolean canBeNegative; // information if the value can be negative
    protected double balance; // balance
    protected int ownerId; // owner id - client id
    private LocalDate localDate;
    double percentage;

    public Service(int ownerId, double percentage) {
        id = IdGenerator.generateServiceId();
        balance = 0;
        history = new History();
        this.ownerId = ownerId;
        this.percentage = percentage;
        localDate = LocalDate.now();
    }

    public Service(double balance, int ownerId, double percentage) {
        id = IdGenerator.generateServiceId();
        this.balance = balance;
        history = new History();
        this.ownerId = ownerId;
        this.percentage = percentage;
        localDate = LocalDate.now();
    }

    /**
     * Getter
     *
     * @return id object of this service
     */
    @Override
    public int getId() {
        return id;
    }


    /**
     * Getter
     *
     * @return owner id of this service
     */
    @Override
    public int getOwnerId() {
        return ownerId;
    }


    /**
     * Getter
     *
     * @return balance of this service
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Getter
     *
     * @return date of the creation
     */
    public LocalDate getLocalDate() {
        return localDate;
    }

    /**
     * Getter
     *
     * @return percentage of service
     */
    public double getPercentage() {
        return percentage;
    }

    /**
     * Setter
     *
     * @param percentage percentage of service
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * Increasing balance
     *
     * @param value value added to balance
     * @return feedback of the success of the operation
     */
    @Override
    public boolean increaseBalance(double value) {
        balance += value;
        return true;
    }

    /**
     * Decreasing balance, considering kind of account
     *
     * @param value value subtracted from balance
     * @return feedback of the success of the operation
     */
    @Override
    public boolean decreaseBalance(double value) {
        if (balance - value < 0 && canBeNegative == false) {
            return false;
        }
        balance -= value;
        return true;
    }

    /**
     * Operation adding element to history of this object
     *
     * @param ack one field of the history
     * @return feedback of the success of the operation
     */
    public boolean addToHistory(Ack ack) {
        return history.add(ack);
    }

    /**
     * Showing whole history of this object
     *
     * @return history of object, in form of list
     */
    public List<Ack> showHistory() {
        return history.returnList();
    }


}
