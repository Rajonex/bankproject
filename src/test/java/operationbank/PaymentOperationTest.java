package operationbank;

import operations.BankAccountOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;

public class PaymentOperationTest {


    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }


    @Test
    public void paymentTest()
    {
        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        double paymentTest = 100.0;
        String descriptionTest = "JUnit Test";
        double balanceTest = bankAccount.getBalance();

        bankAccountOperationTest.payment(bankAccount, paymentTest, descriptionTest);

        Assert.assertEquals(balanceTest+paymentTest, bankAccount.getBalance(), 0.0);
    }

}
