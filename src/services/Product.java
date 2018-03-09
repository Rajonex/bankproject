package services;

import messages.Ack;

public interface Product {
    public boolean addToHistory(Ack ack);
    public boolean increaseBalance(double value);
    public boolean decreaseBalance(double value);
}
