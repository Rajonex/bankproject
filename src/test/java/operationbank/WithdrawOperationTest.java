package operationbank;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;

public class WithdrawOperationTest {


    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }


    @Test
    public void withdrawTest()
    {
        double paymentTest = 100.0;
        String descriptionTest = "JUnit Test";

        WithdrawOperation withdrawOperationTest = new WithdrawOperation(bankAccount, paymentTest, descriptionTest);
        double balanceTest = bankAccount.getBalance();

        withdrawOperationTest.execute();

        Assert.assertEquals(balanceTest-paymentTest, bankAccount.getBalance(), 0.0);
    }
}
