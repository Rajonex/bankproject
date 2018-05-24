package bank;


import clients.Client;
import exceptions.NoSuchAccountException;
import exceptions.NoSuchClientException;
import exceptions.NoSuchCreditException;
import exceptions.NoSuchDepositException;
import interests.InterestA;
import interests.InterestB;
import interests.InterestC;
import messages.BankAck;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.BankAccount;
import services.Credit;
import services.Deposit;

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

    @Test(expected = NoSuchClientException.class)
    public void addNewNormalAccountWithoutClientTest() throws NoSuchClientException {
        boolean ifSucceeded = bankA.addNewNormalAccount(0);
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

    @Test(expected = NoSuchAccountException.class)
    public void makeAccountDebetNotExistingAccount() throws NoSuchAccountException, NoSuchClientException {
        boolean ifSucceeded = bankA.makeAccountDebet(1000, 10_000, 2_000);
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
    public void addNewDepositTest() throws NoSuchClientException, NoSuchAccountException {
        bankA.addNewClient(clientA);
        bankA.addNewNormalAccount(clientA.getId());
        int accountId = bankA.getBankAccounts().get(0).getId();
        BankAccount bankAccount = (BankAccount)bankA.getProductById(accountId);
        bankA.payment(accountId, 15_000);
        boolean ifSucceeded = bankA.addNewDeposit(accountId, 10_000, clientA.getId(), 2, new InterestA());

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void addNewDepositWithNoMoneyTest() throws NoSuchClientException, NoSuchAccountException {
        bankA.addNewClient(clientA);
        bankA.addNewNormalAccount(clientA.getId());
        int accountId = bankA.getBankAccounts().get(0).getId();
        boolean ifSucceeded = bankA.addNewDeposit(accountId, 10_000, clientA.getId(), 2, new InterestA());

        Assert.assertFalse(ifSucceeded);
    }

    @Test(expected = NoSuchClientException.class)
    public void addNewDepositWithoutClientTest() throws NoSuchClientException, NoSuchAccountException {
        boolean ifSucceeded = bankA.addNewDeposit(0, 10_000, 0, 2, new InterestA());
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
        boolean ifSucceeded = bankA.addNewCredit(accountId, 10_000, clientId, new InterestA());

        Assert.assertTrue(ifSucceeded);
    }

    @Test(expected = NoSuchClientException.class)
    public void addNewCreditWithoutClientTest() throws NoSuchClientException, NoSuchAccountException {
        boolean ifSucceeded = bankA.addNewCredit(0, 10_000, 0, new InterestA());
    }

    @Test
    public void deleteCreditByIdSucessTest() throws NoSuchAccountException, NoSuchClientException, NoSuchCreditException {
        bankA.addNewClient(clientA);
        bankA.addNewNormalAccount(clientA.getId());
        bankA.addNewCredit(bankA.getBankAccounts().get(0).getId(), 10_000, clientA.getId(), new InterestA());
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
    public void transferToAnotherBankTest() throws NoSuchClientException, NoSuchAccountException {
        bankA.addNewClient(clientA);
        bankB.addNewClient(clientB);
        int clientAId = bankA.getClients().get(0).getId();
        int clientBId = bankB.getClients().get(0).getId();
        bankA.addNewNormalAccount(clientAId);
        bankB.addNewNormalAccount(clientBId);
        int accountId1 = bankA.getBankAccounts().get(0).getId();
        int accountId2 = bankB.getBankAccounts().get(0).getId();
        bankA.payment(accountId1, 10_000);
        bankA.transferToAnotherBank(accountId1, bankB.getBankId(), accountId2, 3_000);

        BankAccount bankAccountFrom = (BankAccount) bankA.getProductById(accountId1);
        BankAccount bankAccountTo = (BankAccount) bankB.getProductById(accountId2);
        Assert.assertEquals(7_000, bankAccountFrom.getBalance(), 0.1);
        Assert.assertEquals(3_000, bankAccountTo.getBalance(), 0.1);
    }

    @Test
    public void transferToAnotherNotExistingAccountTest() throws NoSuchClientException, NoSuchAccountException {
        bankA.addNewClient(clientA);
        int clientAId = bankA.getClients().get(0).getId();
        bankA.addNewNormalAccount(clientAId);
        int accountId1 = bankA.getBankAccounts().get(0).getId();
        bankA.payment(accountId1, 10_000);
        bankA.transferToAnotherBank(accountId1, bankB.getBankId(), 99, 3_000);
        BankAccount bankAccountFrom = (BankAccount) bankA.getProductById(accountId1);

        Assert.assertEquals(10_000, bankAccountFrom.getBalance(), 0.1);
    }

    @Test
    public void transferToAnotherAccountWithNoMoneyTest() throws NoSuchClientException, NoSuchAccountException {
        bankA.addNewClient(clientA);
        bankB.addNewClient(clientB);
        int clientAId = bankA.getClients().get(0).getId();
        int clientBId = bankB.getClients().get(0).getId();
        bankA.addNewNormalAccount(clientAId);
        bankB.addNewNormalAccount(clientBId);
        int accountId1 = bankA.getBankAccounts().get(0).getId();
        int accountId2 = bankB.getBankAccounts().get(0).getId();
        bankB.payment(accountId2, 1_000);
        bankA.transferToAnotherBank(accountId1, bankB.getBankId(), accountId2, 3_000);
        BankAccount bankAccountFrom = (BankAccount) bankA.getProductById(accountId1);
        BankAccount bankAccountTo = (BankAccount) bankB.getProductById(accountId2);
        Assert.assertEquals(0, bankAccountFrom.getBalance(), 0.1);
        Assert.assertEquals(1_000, bankAccountTo.getBalance(), 0.1);
    }

    @Test
    public void paymentSuccessTest() throws NoSuchClientException, NoSuchAccountException {
        bankA.addNewClient(clientA);
        int clientId = bankA.getClients().get(0).getId();
        bankA.addNewNormalAccount(clientId);
        int accountId = bankA.getBankAccounts().get(0).getId();
        boolean ifSucceeded = bankA.payment(accountId, 10_000);

        Assert.assertTrue(ifSucceeded);
    }

    @Test
    public void paymentTest() throws NoSuchClientException, NoSuchAccountException {
        bankA.addNewClient(clientA);
        int clientId = bankA.getClients().get(0).getId();
        bankA.addNewNormalAccount(clientId);
        int accountId = bankA.getBankAccounts().get(0).getId();
        bankA.payment(accountId, 10_000);

        // TODO - assertEquals...
        BankAccount bankAccount = (BankAccount) bankA.getProductById(accountId);
        Assert.assertThat(bankAccount.getBalance(), is(10_000.0));
    }

    @Test(expected = NoSuchAccountException.class)
    public void paymentNotExistingAccountTest() throws NoSuchAccountException {
        bankA.payment(99, 10_000);
    }

    @Test
    public void withdrawTest() throws NoSuchClientException, NoSuchAccountException {
        bankA.addNewClient(clientA);
        int clientId = bankA.getClients().get(0).getId();
        bankA.addNewNormalAccount(clientId);
        int accountId = bankA.getBankAccounts().get(0).getId();
        bankA.payment(accountId, 10_000);
        BankAccount bankAccount = (BankAccount) bankA.getProductById(accountId);
        bankA.withdraw(accountId, 5_000);
        Assert.assertThat(bankAccount.getBalance(), is(5_000.0));
    }

    @Test(expected = NoSuchAccountException.class)
    public void withdrawNotExistingAccountTest() throws NoSuchAccountException {
        bankA.withdraw(99, 5_000);
    }

    @Test
    public void payCreditRateTest() throws NoSuchClientException, NoSuchAccountException, NoSuchCreditException {
        bankA.addNewClient(clientA);
        bankA.addNewNormalAccount(clientA.getId());
        bankA.addNewCredit(bankA.getBankAccounts().get(0).getId(), 1_000, clientA.getId(), new InterestA());
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
    public void withdrawFromDepositSolvingWithInterestAMechanismTest() throws NoSuchClientException, NoSuchAccountException, NoSuchDepositException {
        bankA.addNewClient(clientA);
        bankA.addNewNormalAccount(clientA.getId());
        int accountId = bankA.getBankAccounts().get(0).getId();
        BankAccount bankAccount = (BankAccount) bankA.getProductById(accountId);
        Assert.assertThat(bankAccount.getBalance(), is(0.0));

        bankA.payment(accountId, 5_000);
        Assert.assertThat(bankAccount.getBalance(), is(5_000.0));

        // Duration set to -1 to simulate thet deposit is expired
        bankA.addNewDeposit(accountId, 5_000, clientA.getId(), -1, new InterestA());
        Deposit deposit = (bankA.getDeposits().get(0));
        Assert.assertThat(deposit.getBalance(), is(5_000.0));
        Assert.assertThat(bankAccount.getBalance(), is(0.0));
        boolean ifSucceeded = bankA.withdrawFromDeposit(deposit.getId());

        Assert.assertTrue(ifSucceeded);
        Assert.assertThat(deposit.getBalance(), is(0.0));
        Assert.assertThat(deposit.getBalance(), is(0.0));
        // There is InterestA mechanism, so interests equal constant 3% of balance so 150 of 5_000
        Assert.assertThat(bankAccount.getBalance(), is(5_150.0));
    }

    @Test
    public void withdrawFromDepositSolvingWithInterestBMechanismTest() throws NoSuchClientException, NoSuchAccountException, NoSuchDepositException {
        bankA.addNewClient(clientA);
        bankA.addNewNormalAccount(clientA.getId());
        int accountId = bankA.getBankAccounts().get(0).getId();
        BankAccount bankAccount = (BankAccount) bankA.getProductById(accountId);
        Assert.assertThat(bankAccount.getBalance(), is(0.0));

        bankA.payment(accountId, 5_000);
        Assert.assertThat(bankAccount.getBalance(), is(5_000.0));

        // Duration set to -1 to simulate thet deposit is expired
        bankA.addNewDeposit(accountId, 5_000, clientA.getId(), -1, new InterestA());
        Deposit deposit = (bankA.getDeposits().get(0));
        Assert.assertThat(deposit.getBalance(), is(5_000.0));
        Assert.assertThat(bankAccount.getBalance(), is(0.0));
        deposit.setInterestsMechanism(new InterestB());
        boolean ifSucceeded = bankA.withdrawFromDeposit(deposit.getId());

        Assert.assertTrue(ifSucceeded);
        Assert.assertThat(deposit.getBalance(), is(0.0));
        // There is InterestB mechanism and balance if above 5_000, so interests equal constant 5% of balance so 250 of 5_000
        Assert.assertThat(bankAccount.getBalance(), is(5_250.0));
    }

    @Test
    public void withdrawFromDepositBreakingTest() throws NoSuchClientException, NoSuchAccountException, NoSuchDepositException {
        bankA.addNewClient(clientA);
        bankA.addNewNormalAccount(clientA.getId());
        int accountId = bankA.getBankAccounts().get(0).getId();
        BankAccount bankAccount = (BankAccount) bankA.getProductById(accountId);
        Assert.assertThat(bankAccount.getBalance(), is(0.0));

        bankA.payment(accountId, 5_000);
        Assert.assertThat(bankAccount.getBalance(), is(5_000.0));

        bankA.addNewDeposit(accountId, 5_000, clientA.getId(), 2, new InterestA());
        Deposit deposit = (bankA.getDeposits().get(0));
        Assert.assertThat(deposit.getBalance(), is(5_000.0));
        Assert.assertThat(bankAccount.getBalance(), is(0.0));
        boolean ifSucceeded = bankA.withdrawFromDeposit(deposit.getId());

        Assert.assertTrue(ifSucceeded);
        Assert.assertThat(deposit.getBalance(), is(0.0));
        // Account is not expired so we do not get interests from deposit
        Assert.assertThat(bankAccount.getBalance(), is(5_000.0));
    }

    @Test(expected = NoSuchDepositException.class)
    public void withdrawFromDepositNotExistingTest() throws NoSuchClientException, NoSuchDepositException {
        bankA.withdrawFromDeposit(99);
    }

    @Test
    public void deleteDepositTest() throws NoSuchClientException, NoSuchAccountException, NoSuchDepositException {
        bankA.addNewClient(clientA);
        bankA.addNewNormalAccount(clientA.getId());
        int accountId = bankA.getBankAccounts().get(0).getId();
        BankAccount bankAccount = (BankAccount) bankA.getProductById(accountId);
        Assert.assertThat(bankAccount.getBalance(), is(0.0));

        bankA.payment(accountId, 5_000);
        Assert.assertThat(bankAccount.getBalance(), is(5_000.0));

        bankA.addNewDeposit(accountId, 5_000, clientA.getId(), 2, new InterestA());
        Deposit deposit = (bankA.getDeposits().get(0));
        Assert.assertNotNull(deposit);
        Assert.assertThat(bankA.getDeposits().size(), is(1));

        boolean ifSucceeded = bankA.deleteDepositById(deposit.getId());
        Assert.assertTrue(ifSucceeded);
        Assert.assertThat(bankA.getDeposits().size(), is(0));
    }

    @Test
    public void changeAccountPercentageTest() throws NoSuchClientException, NoSuchAccountException {
        bankA.addNewClient(clientA);
        bankA.addNewNormalAccount(clientA.getId());
        int accountId = bankA.getBankAccounts().get(0).getId();
        BankAccount bankAccount = (BankAccount) bankA.getProductById(accountId);
        Assert.assertTrue(bankAccount.getInterestsMechanism() instanceof InterestA);

        bankA.changeAccountPercentage(accountId, new InterestB());
        Assert.assertTrue(bankAccount.getInterestsMechanism() instanceof InterestB);

        bankA.changeAccountPercentage(accountId, new InterestC());
        Assert.assertTrue(bankAccount.getInterestsMechanism() instanceof InterestC);
    }

    @Test
    public void changeCreditPercentageTest() throws NoSuchClientException, NoSuchAccountException, NoSuchCreditException {
        bankA.addNewClient(clientA);
        bankA.addNewNormalAccount(clientA.getId());
        int accountId = bankA.getBankAccounts().get(0).getId();
        bankA.addNewCredit(accountId, 10_000, clientA.getId(), new InterestA());
        Credit credit = bankA.getCredits().get(0);
        Assert.assertTrue(credit.getInterestsMechanism() instanceof InterestA);

        bankA.changeCreditPercentage(credit.getId(), new InterestB());
        Assert.assertTrue(credit.getInterestsMechanism() instanceof InterestB);

        bankA.changeCreditPercentage(credit.getId(), new InterestC());
        Assert.assertTrue(credit.getInterestsMechanism() instanceof InterestC);
    }

    // TODO - pecentage wywalić, dć mechnizm!!!!

    @Test
    public void changeDepositPercentageTest() throws NoSuchClientException, NoSuchAccountException, NoSuchCreditException, NoSuchDepositException {
        bankA.addNewClient(clientA);
        bankA.addNewNormalAccount(clientA.getId());
        int accountId = bankA.getBankAccounts().get(0).getId();
        BankAccount bankAccount = (BankAccount)bankA.getProductById(accountId);
        bankA.payment(accountId, 10_000);
        bankA.addNewDeposit(accountId, 5_000, clientA.getId(), 2, new InterestA());
        Deposit deposit = bankA.getDeposits().get(0);
        Assert.assertTrue(deposit.getInterestsMechanism() instanceof InterestA);

        bankA.changeDepositPercentage(deposit.getId(), new InterestB());
        Assert.assertTrue(deposit.getInterestsMechanism() instanceof InterestB);

        bankA.changeDepositPercentage(deposit.getId(), new InterestC());
        Assert.assertTrue(deposit.getInterestsMechanism() instanceof InterestC);
    }

    @Test(expected = NoSuchAccountException.class)
    public void changeAccountPercentageNotExistingTest() throws NoSuchAccountException {
        bankA.changeAccountPercentage(99, new InterestC());
    }
}
