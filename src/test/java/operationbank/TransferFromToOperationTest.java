package operationbank;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;

public class TransferFromToOperationTest {

    static BankAccount bankAccountFrom = null;
    static BankAccount bankAccountTo = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccountFrom = new BankAccount(1000, 0);
        bankAccountTo = new BankAccount(1000, 1);

    }


    @Test
    public void transferFromToTest()
    {
        double valueTest = 100;
        String descriptionTest = "JUnit Test";

        TransferFromToOperation transferFromToOperationTest = new TransferFromToOperation(bankAccountFrom, bankAccountTo, valueTest, descriptionTest);

        transferFromToOperationTest.execute();

        Assert.assertEquals(bankAccountFrom.getBalance(), bankAccountTo.getBalance(), valueTest*2);

    }
}
