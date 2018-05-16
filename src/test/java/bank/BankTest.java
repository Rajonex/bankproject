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

import static org.hamcrest.core.Is.is;

public class BankTest {
    private BankImpl bank;

    @Before
    public void initial() {
        bank = new BankImpl(0);
    }

    @Test
    public void addNewClientSuccessTest() {
        Client client = new Client("Jan", "Kowalski", "12345678912");
        boolean ifSucceeded = bank.addNewClient(client);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void addNewClientAckExistsTest() {
        Client client = new Client("Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        BankAck bankAck = (BankAck) bank.getBankHistory().get(0);
        int clientId = bank.getClients().get(0).getId();

        Assert.assertThat(bankAck.getClientId(), is(clientId));
    }

    @Test
    public void addNewClientNullTest() {
        boolean ifSucceeded = bank.addNewClient(null);

        Assert.assertFalse(ifSucceeded);
    }

    @Test
    public void addNewClientExistingPeselTest() {
        Client client = new Client("Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);

        Client client2 = new Client("Adam", "Nowak", "12345678912");
        boolean ifSucceeded = bank.addNewClient(client2);

        Assert.assertFalse(ifSucceeded);
    }

    @Test
    public void addNewClientTest() throws NoSuchClientException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        Client fromBankClient = bank.getClientById(clientId);
        Assert.assertThat(fromBankClient, is(client));
    }

    @Test(expected = NoSuchClientException.class)
    public void getClientByIdNotExisting() throws NoSuchClientException {
        bank.getClientById(1000);
    }

    @Test
    public void deleteClientByIdSuccessTest() throws NoSuchClientException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        boolean ifSucceeded = bank.deleteClientById(clientId);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void deleteClientByIdNotExistingTest() throws NoSuchClientException {
        boolean ifSuccedded = bank.deleteClientById(1000);

        Assert.assertFalse(ifSuccedded);
    }

    @Test
    public void addNewNormalAccountSuccessTest() throws NoSuchClientException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
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
    public void makeAccountDebetNotExistingAccount() throws NoSuchAccountException, NoSuchClientException {
        boolean ifSucceeded = bank.makeAccountDebet(1000, 10_000, 2_000);

        Assert.assertFalse(ifSucceeded);
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
    public void addNewDepositSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
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

    @Test(expected = NoSuchAccountException.class)
    public void getBankAccountByIdNotExistingAccount() throws NoSuchAccountException {
        bank.getBankAccountById(1000);
    }

    @Test
    public void addNewCreditSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
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
    public void deleteCreditByIdSucessTest() {

    }

    @Test
    public void deleteCreditByIdNotExistingTest() {

    }

    @Test(expected = NoSuchCreditException.class)
    public void deleteCreditByIdNotExistingExceptionTest() throws Exception, NoSuchCreditException {
        throw new NoSuchCreditException("sgeg");
    }

    @Test
    public void transferSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
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
    public void transferWithinOneClientTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
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
        Assert.assertEquals(bankAccountTo.getBalance(), 7_000, 1);
    }

    @Test
    public void transferBetweenTwoClientsTest() {

    }

    @Test
    public void transferFromNotExistingAccountTest() {

    }

    @Test
    public void transferToNotExistingAccountTest() {

    }

    @Test
    public void transferFromAnotherBankSuccessTest() {

    }

    @Test
    public void transferFromAnotherBankToNotExistingAccountTest() {

    }

    @Test
    public void transferToAnotherBankTest() {

    }

    @Test
    public void paymentSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        bank.addNewNormalAccount(clientId);
        int accountId = bank.getBankAccounts().get(0).getId();
        boolean ifSucceeded = bank.payment(accountId, 10_000);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void paymentTest() throws NoSuchClientException, NoSuchAccountException {
        Client client = new Client("Jan", "Kowalski", "12345678912");
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
    public void paymentNotExistingAccountTest() {

    }

    @Test
    public void withdrawSuccessTest() {

    }

    @Test
    public void withdrawNotExistingAccountTest() {

    }

    @Test
    public void withdrawTest() {

    }

    @Test
    public void payCreditRateSuccessTest() {

    }

    @Test
    public void payCreditRateNotExistingTest() {

    }

    @Test
    public void payCreditRateTest() {

    }

    @Test
    public void withdrawFromDepositSuccessTest() {

    }

    @Test
    public void withdrawFromDepositNotExistingTest() {

    }

    @Test
    public void withdrawFromDepositTest() {

    }

    @Test
    public void changeAccountPercentageSuccessTest() {

    }

    @Test
    public void changeAccountPercentageNotExistingTest() {

    }

    @Test
    public void changeAccountPercentage() {

    }
}
