package operationbank;

import bank.BankImpl;
import clients.Client;
import exceptions.NoSuchAccountException;
import exceptions.NoSuchClientException;
import messages.Ack;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.DebetAccountDecorator;
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
        if(!bank.addNewNormalAccount(clientId))
        {
            throw new NoSuchAccountException("Nie utworzono konta");
        }
        bankAccount = bank.getBankAccounts().get(0);
        bank.payment(bankAccount.getId(), 1000);
    }

    @Test
    public void makeAccountDebetTest() throws NoSuchAccountException {
        String descriptionTest = "JUnit Test";
        int accountId = bankAccount.getId();
        MakeAccountDebetOperation makeAccountDebetOperation = new MakeAccountDebetOperation(bank, bankAccount, 500, descriptionTest);
        Ack ack = makeAccountDebetOperation.execute();
//        if(ack != null)
//        {
//            throw new NoSuchAccountException(ack.getTypeOperation().toString());
//        }
        bankAccount = bank.getProductById(accountId);
        boolean result = bankAccount.decreaseBalance(1200);
//        bank.withdraw(accountId, 1200);

//        Assert.assertTrue(bankAccount instanceof DebetAccountDecorator);
//        Assert.assertEquals(-200, bankAccount.getBalance(), 0.1);
        Assert.assertTrue(result);
    }
}
