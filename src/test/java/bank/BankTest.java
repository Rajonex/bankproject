package bank;


import clients.Client;
import history.History;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BankTest {
    private History history;
    private Bank bank;

    @Before
    public void initial() {
        history = new History();
        bank = new BankA(0);
    }

    @Test
    public void addNewClientSuccessTest() {
        Client client = new Client("Jan", "Kowalski", "12345678912");
        boolean ifSucceeded = bank.addNewClient(client);

        Assert.assertTrue(ifSucceeded);
    }
}
