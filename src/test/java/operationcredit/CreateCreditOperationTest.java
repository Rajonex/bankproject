package operationcredit;

import interests.InterestA;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;

public class CreateCreditOperationTest {

    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0, new InterestA());
    }


    @Test
    public void createCreditTest() {

        double balanceTest = bankAccount.getBalance();
        double balance = 1000.0;
        String descriptionTest = "JUnit Test";
        CreateCreditOperation createCreditOperationTest = new CreateCreditOperation(bankAccount, balance, bankAccount.getOwnerId(), new InterestA(), descriptionTest);

        createCreditOperationTest.execute();

        int ownerIdTest = bankAccount.getOwnerId();

        Assert.assertEquals(bankAccount.getBalance(), balanceTest+balance, 0.01);



    }
}