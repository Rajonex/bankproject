package main.java.messages;

import main.java.services.Product;

import java.time.LocalDate;

public class Ack {
    Product product1;
    Product product2;
    TypeOperation typeOperation;
    LocalDate localDate;
    String description;

    public Ack(Product product1, Product product2, TypeOperation typeOperation, LocalDate localDate, String description) {
        this.product1 = product1;
        this.product2 = product2;
        this.typeOperation = typeOperation;
        this.localDate = localDate;
        this.description = description;
    }
}
