package bank;


import clients.Client;
import history.History;
import org.junit.Assert;
import org.junit.Test;

public class BankTest
{
    private History history = new History();
    private Bank bank = new BankA();

    @Test
    public void addNewClientTest()
    {
        Client client = new Client("Jana", "Kowalski", "12345678912");
        boolean ifSucceeded = bank.addNewClient(client);

        Assert.assertEquals(ifSucceeded, true);
    }
}
