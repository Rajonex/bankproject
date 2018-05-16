package messages;


import java.time.LocalDate;

public class Ack {
    protected Integer product1;
    protected  Integer product2;
    protected  TypeOperation typeOperation;
    protected  LocalDate localDate;
    protected  String description;

    public Integer getProduct1() {
        return product1;
    }

    public Integer getProduct2() {
        return product2;
    }

    public TypeOperation getTypeOperation() {
        return typeOperation;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public String getDescription() {
        return description;
    }

    public Ack(Integer product1, Integer product2, TypeOperation typeOperation, LocalDate localDate, String description) {
        this.product1 = product1;
        this.product2 = product2;
        this.typeOperation = typeOperation;
        this.localDate = localDate;
        this.description = description;
    }
}
