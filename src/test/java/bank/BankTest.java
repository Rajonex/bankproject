package bank;


import clients.Client;
import exceptions.NoSuchAccountException;
import exceptions.NoSuchClientException;
import exceptions.NoSuchCreditException;
import messages.BankAck;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.BankAccount;
import services.Credit;

import static org.hamcrest.core.Is.is;

public class BankTest {
    private PaymentSystemInfrastructure paymentSystemInfrastructure;
    private BankImpl bankA;
    private BankImpl bankB;
    private Client clientA;
    private Client clientB;

    @Before
    public void initial() {
        paymentSystemInfrastructure = new PaymentSystemInfrastructure();
        bankA = paymentSystemInfrastructure.createNewBank();
        bankB = paymentSystemInfrastructure.createNewBank();
        clientA = new Client("Jan", "Kowalski", "12345678912");
        clientB = new Client("Adam", "Nowak", "12345678913");
    }

    @Test
    public void addNewClientSuccessTest() {
        boolean ifSucceeded = bankA.addNewClient(clientA);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void addNewClientAckExistsTest() {
        bankA.addNewClient(clientA);
        BankAck bankAck = (BankAck) bankA.getBankHistory().get(0);
        int clientId = bankA.getClients().get(0).getId();

        Assert.assertThat(bankAck.getClientId(), is(clientId));
    }

    @Test
    public void addNewClientNullTest() {
        boolean ifSucceeded = bankA.addNewClient(null);

        Assert.assertFalse(ifSucceeded);
    }

    @Test
    public void addNewClientExistingPeselTest() {
        bankA.addNewClient(clientA);
        Client clientWithSamePesel = new Client("Test", "Test", clientA.getPesel());
        boolean ifSucceeded = bankA.addNewClient(clientWithSamePesel);

        Assert.assertFalse(ifSucceeded);
    }

    @Test
    public void addNewClientTest() throws NoSuchClientException {
        bankA.addNewClient(clientA);
        int clientId = bankA.getClients().get(0).getId();
        Client fromBankClient = bankA.getClientById(clientId);
        Assert.assertThat(fromBankClient, is(clientA));
    }

    @Test(expected = NoSuchClientException.class)
    public void getClientByIdNotExisting() throws NoSuchClientException {
        bankA.getClientById(1000);
    }

    @Test
    public void deleteClientByIdSuccessTest() throws NoSuchClientException {
        bankA.addNewClient(clientA);
        int clientId = bankA.getClients().get(0).getId();
        boolean ifSucceeded = bankA.deleteClientById(clientId);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void deleteClientByIdNotExistingTest() throws NoSuchClientException {
        boolean ifSuccedded = bankA.deleteClientById(1000);

        Assert.assertFalse(ifSuccedded);
    }

    @Test
    public void addNewNormalAccountSuccessTest() throws NoSuchClientException {
        bankA.addNewClient(clientA);
        int clientId = bankA.getClients().get(0).getId();
        boolean ifSucceeded = bankA.addNewNormalAccount(clientId);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void addNewNormalAccountWithoutClientTest() throws NoSuchClientException {
        boolean ifSucceeded = bankA.addNewNormalAccount(0);

        Assert.assertFalse(ifSucceeded);
    }

    @Test
    public void makeAccountDebetSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        bankA.addNewClient(clientA);
        int clientId = bankA.getClients().get(0).getId();
        bankA.addNewNormalAccount(clientId);
        int accountId = bankA.getBankAccounts().get(0).getId();
        boolean ifSucceeded = bankA.makeAccountDebet(accountId, 10_000, 2_000);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void makeAccountDebetNotExistingAccount() throws NoSuchAccountException, NoSuchClientException {
        boolean ifSucceeded = bankA.makeAccountDebet(1000, 10_000, 2_000);

        Assert.assertFalse(ifSucceeded);
    }

    @Test
    public void addNewDebetAccountSuccessTest() throws NoSuchClientException {
        bankA.addNewClient(clientA);
        int clientId = bankA.getClients().get(0).getId();
        boolean ifSucceeded = bankA.addNewDebetAccount(clientId, 10_000, 2_000, 5);

        Assert.assertTrue(ifSucceeded);
    }

    @Test(expected = NoSuchClientException.class)
    public void addNewDebetAccountWithoutClientTest() throws NoSuchClientException {
        boolean ifSucceeded = bankA.addNewDebetAccount(0, 10_000, 2_000, 5);
    }

    @Test
    public void addNewDepositSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        bankA.addNewClient(clientA);
        int clientId = bankA.getClients().get(0).getId();
        bankA.addNewNormalAccount(clientId);
        int accountId = bankA.getBankAccounts().get(0).getId();
        boolean ifSucceeded = bankA.addNewDeposit(accountId, 10_000, clientId, 5);

        Assert.assertTrue(ifSucceeded);
    }

    @Test(expected = NoSuchClientException.class)
    public void addNewDepositWithoutClientTest() throws NoSuchClientException, NoSuchAccountException {
        boolean ifSucceeded = bankA.addNewDeposit(0, 10_000, 0, 5);
    }

    @Test(expected = NoSuchAccountException.class)
    public void getBankAccountByIdNotExistingAccount() throws NoSuchAccountException {
        bankA.getProductById(1000);
    }

    @Test
    public void addNewCreditSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        bankA.addNewClient(clientA);
        int clientId = bankA.getClients().get(0).getId();
        bankA.addNewNormalAccount(clientId);
        int accountId = bankA.getBankAccounts().get(0).getId();
        boolean ifSucceeded = bankA.addNewCredit(accountId, 10_000, clientId, 5);

        Assert.assertTrue(ifSucceeded);
    }

    @Test(expected = NoSuchClientException.class)
    public void addNewCreditWithoutClientTest() throws NoSuchClientException, NoSuchAccountException {
        boolean ifSucceeded = bankA.addNewCredit(0, 10_000, 0, 5);
    }

    @Test
    public void deleteCreditByIdSucessTest() throws NoSuchAccountException, NoSuchClientException, NoSuchCreditException {
        bankA.addNewClient(clientA);
        bankA.addNewNormalAccount(clientA.getId());
        bankA.addNewCredit(bankA.getBankAccounts().get(0).getId(), 10_000, clientA.getId(), 5);
        boolean ifSucceeded = bankA.deleteCreditById(bankA.getCredits().get(0).getId());

        Assert.assertTrue(ifSucceeded);
    }

    @Test(expected = NoSuchCreditException.class)
    public void deleteCreditByIdNotExistingTest() throws NoSuchCreditException, NoSuchClientException {
        bankA.deleteCreditById(999);
    }

    @Test(expected = NoSuchCreditException.class)
    public void deleteCreditByIdNotExistingExceptionTest() throws Exception, NoSuchCreditException {
        throw new NoSuchCreditException("test exception");
    }

    @Test
    public void transferSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
        bankA.addNewClient(client);
        int clientId1 = bankA.getClients().get(0).getId();
        bankA.addNewNormalAccount(clientId1);
        bankA.addNewNormalAccount(clientId1);
        int accountId1 = bankA.getBankAccounts().get(0).getId();
        int accountId2 = bankA.getBankAccounts().get(1).getId();
        bankA.payment(accountId1, 10_000);
        boolean ifSucceeded = bankA.transfer(accountId1, accountId2, 5_000);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void transferWithinOneClientTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
        bankA.addNewClient(client);
        int clientId = bankA.getClients().get(0).getId();
        bankA.addNewNormalAccount(clientId);
        bankA.addNewNormalAccount(clientId);
        int accountId1 = bankA.getBankAccounts().get(0).getId();
        int accountId2 = bankA.getBankAccounts().get(1).getId();
        bankA.payment(accountId1, 10_000);
        bankA.transfer(accountId1, accountId2, 7_000);

        BankAccount bankAccountFrom = (BankAccount) bankA.getProductById(accountId1);
        BankAccount bankAccountTo = (BankAccount) bankA.getProductById(accountId2);
        Assert.assertEquals(bankAccountFrom.getBalance(), 3_000, 1);
        Assert.assertEquals(bankAccountTo.getBalance(), 7_000, 1);
    }

    @Test
    public void transferBetweenTwoClientsTest() throws NoSuchClientException, NoSuchAccountException {
        Client clientA = new Client("Jan", "Kowalski", "12345678912");
        Client clientB = new Client("John", "Black", "12345678913");
        bankA.addNewClient(clientA);
        bankA.addNewClient(clientB);
        int clientAId = bankA.getClients().get(0).getId();
        int clientBId = bankA.getClients().get(1).getId();
        bankA.addNewNormalAccount(clientAId);
        bankA.addNewNormalAccount(clientBId);
        int accountId1 = bankA.getBankAccounts().get(0).getId();
        int accountId2 = bankA.getBankAccounts().get(1).getId();
        bankA.payment(accountId1, 10_000);
        bankA.transfer(accountId1, accountId2, 7_000);

        BankAccount bankAccountFrom = (BankAccount) bankA.getProductById(accountId1);
        BankAccount bankAccountTo = (BankAccount) bankA.getProductById(accountId2);
        Assert.assertEquals(bankAccountFrom.getBalance(), 3_000, 1);
        Assert.assertEquals(bankAccountTo.getBalance(), 7_000, 1);
    }

    @Test(expected = NoSuchAccountException.class)
    public void transferFromNotExistingAccountTest() throws NoSuchAccountException {
        bankA.transfer(999, 999, 10_000);
    }

    @Test
    public void transferToNotExistingAccountTest() {
        Assert.assertTrue(false);
    }

    @Test
    public void transferFromAnotherBankSuccessTest() {
        Assert.assertTrue(false);
    }

    @Test
    public void transferFromAnotherBankToNotExistingAccountTest() {
        Assert.assertTrue(false);
    }

    @Test
    public void transferToAnotherBankTest() {
        Assert.assertTrue(false);
    }

    @Test
    public void paymentSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
        bankA.addNewClient(client);
        int clientId = bankA.getClients().get(0).getId();
        bankA.addNewNormalAccount(clientId);
        int accountId = bankA.getBankAccounts().get(0).getId();
        boolean ifSucceeded = bankA.payment(accountId, 10_000);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void paymentTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
        bankA.addNewClient(client);
        int clientId = bankA.getClients().get(0).getId();
        bankA.addNewNormalAccount(clientId);
        int accountId = bankA.getBankAccounts().get(0).getId();
        bankA.payment(accountId, 10_000);

        // TODO - assertEquals...
        BankAccount bankAccount = (BankAccount) bankA.getProductById(accountId);
        Assert.assertEquals(bankAccount.getBalance(), 10_000, 1);
    }


    @Test
    public void paymentNotExistingAccountTest() {
        Assert.assertTrue(false);
    }

    @Test
    public void withdrawSuccessTest() {
        Assert.assertTrue(false);
    }

    @Test
    public void withdrawNotExistingAccountTest() {
        Assert.assertTrue(false);
    }

    @Test
    public void withdrawTest() {
        Assert.assertTrue(false);
    }

    @Test
    public void payCreditRateTest() throws NoSuchClientException, NoSuchAccountException, NoSuchCreditException {
        bankA.addNewClient(clientA);
        bankA.addNewNormalAccount(clientA.getId());
        bankA.addNewCredit(bankA.getBankAccounts().get(0).getId(), 1_000, clientA.getId(), 5);
        Credit credit = bankA.getCredits().get(0);
        boolean ifSucceeded = bankA.payCreditRate(bankA.getCredits().get(0).getId(), 300);

        Assert.assertTrue(ifSucceeded);
        Assert.assertThat(credit.getBalance(), is(-700.0));
        Assert.assertThat(credit.getBankAccount().getBalance(), is(700.0));
    }

    @Test(expected = NoSuchCreditException.class)
    public void payCreditRateNotExistingTest() throws NoSuchCreditException, NoSuchClientException {
        boolean ifSucceeded = bankA.payCreditRate(999, 3_000);
    }

    @Test
    public void withdrawFromDepositSuccessTest() {
        Assert.assertTrue(false);
    }

    @Test
    public void withdrawFromDepositNotExistingTest() {
        Assert.assertTrue(false);
    }

    @Test
    public void withdrawFromDepositTest() {
        Assert.assertTrue(false);
    }

    @Test
    public void changeAccountPercentageSuccessTest() {
        Assert.assertTrue(false);
    }

    @Test
    public void changeAccountPercentageNotExistingTest() {
        Assert.assertTrue(false);
    }

    @Test
    public void changeAccountPercentage() {
        Assert.assertTrue(false);
    }
}
