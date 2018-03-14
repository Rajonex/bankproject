package messages;

import clients.Client;
import services.Product;

import java.time.LocalDate;

public class BankAck extends Ack
{
    private Client client;

    /**
     * Getters
     * @return
     */
    public Client getClient()
    {
        return client;
    }

    /**
     * Setter
     * @param client
     */
    public void setClient(Client client)
    {
        this.client = client;
    }

    /**
     * Constructor
     * @param product1
     * @param product2
     * @param client
     * @param typeOperation
     * @param localDate
     * @param description
     */
    public BankAck(Product product1, Product product2, Client client, TypeOperation typeOperation, LocalDate localDate, String description)
    {
        super(product1, product2, typeOperation, localDate, description);
        this.client = client;
    }
}
