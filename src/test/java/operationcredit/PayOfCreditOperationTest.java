package operationcredit;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.Credit;

public class PayOfCreditOperationTest {

    static BankAccount bankAccount = null;
    static Credit credit = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
        credit = new Credit(bankAccount, 100, 0);
    }



    @Test
    public void payOfCreditTest()
    {

        String descriptionTest = "JUnit Test";

        PayOfCreditOperation payOfCreditOperationTest = new PayOfCreditOperation(credit, descriptionTest);
        double balanceTest = credit.getBalance();

        payOfCreditOperationTest.execute();

        Assert.assertEquals(balanceTest, credit.getBalance(), balanceTest);
    }
}
