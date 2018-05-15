package operationcredit;

import messages.Ack;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.Credit;

public class CreateCreditOperationTest {

    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }


    @Test
    public void createCreditTest() {

        double balanceTest = bankAccount.getBalance();
        double balance = 1000.0;
        String descriptionTest = "JUnit Test";
        CreateCreditOperation createCreditOperationTest = new CreateCreditOperation(bankAccount, balance, bankAccount.getOwnerId(), descriptionTest);

        createCreditOperationTest.execute();

        int ownerIdTest = bankAccount.getOwnerId();

        Assert.assertEquals(bankAccount.getBalance(), balanceTest+balance, 0.01);



    }
}
