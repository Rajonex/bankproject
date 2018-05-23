package operationbank;

import bank.BankImpl;
import clients.Client;
import exceptions.NoSuchAccountException;
import exceptions.NoSuchClientException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.Product;


// TODO - pobrac z banku konto do testu, zeby podac je do metody
public class MakeAccountDebetOperationTest {

    static Product bankAccount = null;
    static BankImpl bank = null;


    @BeforeClass
    static public void newBankAccountTest() throws NoSuchClientException, NoSuchAccountException {
        bank = new BankImpl(1);
        Client client = new Client("Jan", "Kowalski", "12345678912");
        bank.addNewClient(client);
        int clientId = bank.getClients().get(0).getId();
        bank.addNewNormalAccount(clientId);
        bankAccount = bank.getBankAccounts().get(0);
        bank.payment(bankAccount.getId(), 1000);
    }

    @Test
    public void makeAccountDebetTest() throws NoSuchAccountException {
        String descriptionTest = "JUnit Test";
        int accountId = bank.getBankAccounts().get(0).getId();
        MakeAccountDebetOperation makeAccountDebetOperation = new MakeAccountDebetOperation(bank, bankAccount, 500, descriptionTest);
        makeAccountDebetOperation.execute();
        bankAccount = bank.getBankAccountById(accountId);
        boolean result = bankAccount.decreaseBalance(1200);
//        bank.withdraw(accountId, 1200);

//        Assert.assertTrue(bankAccount instanceof DebetAccountDecorator);
        Assert.assertEquals(-200, bankAccount.getBalance(), 0.1);
//        Assert.assertTrue(result);
    }
}
