package services;

import history.History;

public class Service implements Product {
    private History history;
    private int id;
    private boolean canBeNegative; // information if the value can be negative

    private double balance;
}
