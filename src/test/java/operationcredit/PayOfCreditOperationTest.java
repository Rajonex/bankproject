package operationcredit;

import operations.CreditOperation;
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
        CreditOperation creditOperationTest = new CreditOperation();
        String descriptionTest = "JUnit Test";
        double balanceTest = credit.getBalance();

        creditOperationTest.payOfCredit(credit, descriptionTest);

        Assert.assertEquals(balanceTest, credit.getBalance(), credit.getBalance());
    }
}
