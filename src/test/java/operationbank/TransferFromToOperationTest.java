package operationbank;

import operations.BankAccountOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;

public class TransferFromToOperationTest {

    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }


    @Test
    public void transferFromToTest()
    {
        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        BankAccount bankAccountCopyTest = bankAccount;
        double valueTest = 100;
        String descriptionTest = "JUnit Test";

        bankAccountOperationTest.transferFromTo(bankAccount, bankAccountCopyTest, valueTest, descriptionTest);

        Assert.assertEquals(bankAccount.getBalance(), bankAccountCopyTest.getBalance(), valueTest*2);

    }
}
