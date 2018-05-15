package operationbank;

import interests.InterestB;
import interests.InterestsMechanism;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;

public class ChangePercentageOperationTest {

    static BankAccount bankAccount = null;
    static InterestsMechanism interestsMechanism;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
        interestsMechanism = new InterestB();
    }


    @Test
    public void changePercentageTest() {
        String descriptionTest = "JUnit Test";
        ChangePercentageOperation changePercentageOperationTest = new ChangePercentageOperation(bankAccount, interestsMechanism, descriptionTest);

        changePercentageOperationTest.execute();

        Assert.assertEquals(interestsMechanism, bankAccount.getInterestsMechanism());

    }
}