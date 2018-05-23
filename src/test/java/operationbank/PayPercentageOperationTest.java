package operationbank;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;

public class PayPercentageOperationTest {

    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }



    @Test
    public void payPercentageTest() {

        String descriptionTest = "JUnit Test";
        PayPercentageOperation payPercentageOperationTest = new PayPercentageOperation(bankAccount, descriptionTest);

        double balanceTest = bankAccount.getBalance(); //stary balance
        double percentageTest = bankAccount.getInterests(); //suma o jaka powinien wzrosnac credit

        payPercentageOperationTest.execute(); //wywolanie operacji na pierwotnym kredycie za posrednictwem creditOperation

        Assert.assertEquals(balanceTest+percentageTest, bankAccount.getBalance(), 0.01);
    }
}