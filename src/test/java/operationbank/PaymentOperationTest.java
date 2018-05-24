package operationbank;

import interests.InterestA;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;

public class PaymentOperationTest {


    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0, new InterestA());
    }


    @Test
    public void paymentTest()
    {
        double valueTest = 100.0;
        double oldBalance = bankAccount.getBalance();
        String descriptionTest = "JUnit Test";
        PaymentOperation paymentOperationTest = new PaymentOperation(bankAccount, valueTest, descriptionTest);
        paymentOperationTest.execute();


        Assert.assertEquals(bankAccount.getBalance()-valueTest, oldBalance, 0.01);

    }

}