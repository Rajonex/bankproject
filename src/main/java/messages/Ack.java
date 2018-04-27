package messages;


import services.Product;

import java.time.LocalDate;

public class Ack {
    int product1;
    int product2;
    TypeOperation typeOperation;
    LocalDate localDate;
    String description;

    public Ack(int product1, int product2, TypeOperation typeOperation, LocalDate localDate, String description) {
        this.product1 = product1;
        this.product2 = product2;
        this.typeOperation = typeOperation;
        this.localDate = localDate;
        this.description = description;
    }
}
