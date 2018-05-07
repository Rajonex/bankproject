package messages;


import java.time.LocalDate;

public class Ack {
    Integer product1;
    Integer product2;
    TypeOperation typeOperation;
    LocalDate localDate;
    String description;

    public Ack(Integer product1, Integer product2, TypeOperation typeOperation, LocalDate localDate, String description) {
        this.product1 = product1;
        this.product2 = product2;
        this.typeOperation = typeOperation;
        this.localDate = localDate;
        this.description = description;
    }
}
