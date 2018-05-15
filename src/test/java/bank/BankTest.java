package bank;


import clients.Client;
import exceptions.NoSuchAccountException;
import exceptions.NoSuchClientException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.BankAccount;

public class BankTest {
    //    private History history;
    private BankA bank;

    @Before
    public void initial() {
//        history = new History();
        bank = new BankA(0);
    }

    @Test
    public void addNewClientSuccessTest() {
        Client client = new Client( "Jan", "Kowalski", "12345678912");
        boolean ifSucceeded = bank.addNewClient(client);

        Assert.assertTrue(ifSucceeded);
    }

//    @Test
//    public void addNewClientTest() {
//        Client client = new Client(0, "Jan", "Kowalski", "12345678912");
//        bank.addNewClient(client);
//        Client fromBankClient = bank.getClientById(0);
//        Assert.assertThat(fromBankClient, is(client));
//    }

    @Test
    public void deleteClientByIdSuccessTest() throws NoSuchClientException {
        Client client = new Client( "Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        boolean ifSucceeded = bank.deleteClientById(clientId);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void addNewNormalAccountSuccessTest() throws NoSuchClientException {
        Client client = new Client( "Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        boolean ifSucceeded = bank.addNewNormalAccount(clientId);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void addNewNormalAccountWithoutClientTest() throws NoSuchClientException {
        boolean ifSucceeded = bank.addNewNormalAccount(0);

        Assert.assertFalse(ifSucceeded);
    }

    @Test
    public void makeAccountDebetSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        bank.addNewNormalAccount(clientId);
        int accountId = bank.getBankAccounts().get(0).getId();
        boolean ifSucceeded = bank.makeAccountDebet(accountId, 10_000, 2_000);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void addNewDebetAccountSuccessTest() throws NoSuchClientException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        boolean ifSucceeded = bank.addNewDebetAccount(clientId, 10_000, 2_000, 5);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void addNewDebetAccountWithoutClientTest() throws NoSuchClientException {
        boolean ifSucceeded = bank.addNewDebetAccount(0, 10_000, 2_000, 5);

        Assert.assertFalse(ifSucceeded);
    }

    @Test
    public void makeAccountNormalSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client( "Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        bank.addNewDebetAccount(clientId, 10_000, 2_000, 5);

        int accountId = bank.getBankAccounts().get(0).getId();
        boolean ifSucceeded = bank.makeAccountNormal(accountId);

        Assert.assertTrue(ifSucceeded);
    }


    @Test
    public void addNewDepositSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client( "Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        bank.addNewNormalAccount(clientId);
        int accountId = bank.getBankAccounts().get(0).getId();
        boolean ifSucceeded = bank.addNewDeposit(accountId, 10_000, clientId, 5);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void addNewDepositWithoutClientTest() throws NoSuchClientException, NoSuchAccountException {
        boolean ifSucceeded = bank.addNewDeposit(0, 10_000, 0, 5);

        Assert.assertFalse(ifSucceeded);
    }

    @Test
    public void addNewCreditSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client( "Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        bank.addNewNormalAccount(clientId);
        int accountId = bank.getBankAccounts().get(0).getId();
        boolean ifSucceeded = bank.addNewCredit(accountId, 10_000, clientId, 5);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void addNewCreditWithoutClientTest() throws NoSuchClientException, NoSuchAccountException {
        boolean ifSucceeded = bank.addNewCredit(0, 10_000, 0, 5);

        Assert.assertFalse(ifSucceeded);
    }

    @Test
    public void paymentSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client( "Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        bank.addNewNormalAccount(clientId);
        int accountId = bank.getBankAccounts().get(0).getId();
        boolean ifSucceeded = bank.payment(accountId, 10_000);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void paymentTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client( "Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        bank.addNewNormalAccount(clientId);
        int accountId = bank.getBankAccounts().get(0).getId();
        bank.payment(accountId, 10_000);

        // TODO - assertEquals...
        BankAccount bankAccount = bank.getBankAccountById(accountId);
        Assert.assertEquals(bankAccount.getBalance(), 10_000, 1);
    }

    @Test
    public void transferSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client( "Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId1 = bank.getClients().get(0).getId();
        bank.addNewNormalAccount(clientId1);
        bank.addNewNormalAccount(clientId1);
        int accountId1 = bank.getBankAccounts().get(0).getId();
        int accountId2 = bank.getBankAccounts().get(1).getId();
        bank.payment(accountId1, 10_000);
        boolean ifSucceeded = bank.transfer(accountId1, accountId2, 5_000);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void transferTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client( "Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        bank.addNewNormalAccount(clientId);
        bank.addNewNormalAccount(clientId);
        int accountId1 = bank.getBankAccounts().get(0).getId();
        int accountId2 = bank.getBankAccounts().get(1).getId();
        bank.payment(accountId1, 10_000);
        bank.transfer(accountId1, accountId2, 7_000);

        BankAccount bankAccountFrom = bank.getBankAccountById(accountId1);
        BankAccount bankAccountTo = bank.getBankAccountById(accountId2);
        Assert.assertEquals(bankAccountFrom.getBalance(), 3_000, 1);
        Assert.assertEquals(bankAccountTo.getBalance(), 7_000,1 );
    }
}
