package operationcredit;

import interests.InterestA;
import interests.InterestsMechanism;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.Credit;

public class ChangePercentageOperationTest {

    static BankAccount bankAccount = null;
    static Credit credit = null;
    static InterestsMechanism interestsMechanism = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
        credit = new Credit(bankAccount, 100, 0);
        interestsMechanism = new InterestA();
    }


    @Test
    public void changePercentageTest() {
        String descriptionTest = "JUnit Test";

        ChangePercentageOperation changePercentageOperationTest = new ChangePercentageOperation(credit, interestsMechanism, descriptionTest);

        changePercentageOperationTest.execute();

        Assert.assertEquals(interestsMechanism, credit.getInterestsMechanism());

    }

}
