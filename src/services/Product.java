package services;

import messages.Ack;

public interface Product {
    /**
     * Increasing balance
     * @param value value added to balance
     * @return feedback of the success of the operation
     */
    public boolean increaseBalance(double value);

    /**
     * Decreasing balance, considering kind of account
     * @param value value subtracted from balance
     * @return feedback of the success of the operation
     */
    public boolean decreaseBalance(double value);
}
