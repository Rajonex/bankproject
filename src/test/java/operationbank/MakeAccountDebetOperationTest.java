package operationbank;

import operations.BankAccountOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;

public class MakeAccountDebetOperationTest {

    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }

    @Test
    public void makeAccountDebetTest()
    {

//        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
//        double limitTest = 1000.0;
//        String descriptionTest = "JUnit Test";
//
//        BankAccount bankAccount1Test = bankAccount;
//        bankAccount1Test = new DebetAccountDecorator(bankAccount1Test, limitTest);
//
//        bankAccountOperationTest.makeAccountDebet(bankAccount, limitTest, descriptionTest);
//
//        Assert.assertEquals(bankAccount, bankAccount1Test);

        double limitTest = 1000.0;
        String descriptionTest = "JUnit Test";

        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        boolean isCreated = bankAccountOperationTest.makeAccountDebet(bankAccount, limitTest, descriptionTest);
        Assert.assertTrue(isCreated);
    }
}
