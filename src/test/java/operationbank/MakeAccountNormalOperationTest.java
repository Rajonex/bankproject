package operationbank;

import operations.BankAccountOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;

public class MakeAccountNormalOperationTest {


    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }


    @Test
    public void makeAccountNormalTest()
    {

//        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
//        double limitTest = 1000.0;
//        String descriptionTest = "JUnit Test";
//
//        BankAccount bankAccount1Test = bankAccount;
//        bankAccount1Test = new NormalAccount(bankAccount1Test);
//
//        bankAccountOperationTest.makeAccountNormal(bankAccount, descriptionTest);
//
//        Assert.assertEquals(bankAccount, bankAccount1Test);


        String descriptionTest = "JUnit Test";

        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        boolean isCreated = bankAccountOperationTest.makeAccountNormal(bankAccount, descriptionTest);
        Assert.assertTrue(isCreated);

    }
}
