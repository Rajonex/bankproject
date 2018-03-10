package services;

import history.History;
import messages.Ack;

import java.util.List;

public class Service implements Product {
    private History history; // history of this service
    private int id; // id this service object
    protected boolean canBeNegative; // information if the value can be negative
    protected double balance; // balance
    protected int ownerId; // owner id - client id
    private static int number = 0; // unique number for all objects of class Service

    public Service(int ownerId) {
        id = number++;
        balance = 0;
        history = new History();
        this.ownerId = ownerId;
    }

    public Service(double balance, int ownerId) {
        id = number++;
        this.balance = balance;
        history = new History();
        this.ownerId = ownerId;
    }

    /**
     * Getter
     * @return id object of this service
     */
    public int getId() {
        return id;
    }

    /**
     * Getter
     * @return balance of this service
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Increasing balance
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
     * @param value value subtracted from balance
     * @return feedback of the success of the operation
     */
    @Override
    public boolean decreaseBalance(double value) {
        if(balance-value < 0 && canBeNegative == false)
        {
            return false;
        }
        balance -= value;
        return true;
    }

    /**
     * Operation adding element to history of this object
     * @param ack one field of the history
     * @return feedback of the success of the operation
     */
    public boolean addToHistory(Ack ack) {
        return history.add(ack);
    }

    /**
     * Showing whole history of this object
     * @return history of object, in form of list
     */
    public List<Ack> showHistory() {
        return history.returnList();
    }


}
