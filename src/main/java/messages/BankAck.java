package messages;


import java.time.LocalDate;

public class BankAck extends Ack {
    // TODO - change to long to extend range
    private Integer clientId;

    /**
     * Getters
     *
     * @return
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * Setter
     *
     * @param clientId
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * Constructor
     *
     * @param product1
     * @param product2
     * @param clientId
     * @param typeOperation
     * @param localDate
     * @param description
     */
    public BankAck(Integer product1, Integer product2, Integer clientId,TypeOperation typeOperation, LocalDate localDate, String description) {
        super(product1, product2, typeOperation, localDate, description);
        this.clientId = clientId;
    }
}
