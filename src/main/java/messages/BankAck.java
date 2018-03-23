package messages;


import services.Product;

import java.time.LocalDate;

public class BankAck extends Ack {
    // TODO - change to long to extend range
    private int clientId;

    /**
     * Getters
     *
     * @return
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Setter
     *
     * @param clientId
     */
    public void setClientId(int clientId) {
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
    public BankAck(Product product1, Product product2, int clientId,TypeOperation typeOperation, LocalDate localDate, String description) {
        super(product1, product2, typeOperation, localDate, description);
        this.clientId = clientId;
    }
}
