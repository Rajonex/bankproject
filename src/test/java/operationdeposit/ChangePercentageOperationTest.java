package operationdeposit;

import interests.InterestA;
import interests.InterestsMechanism;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.Deposit;

public class ChangePercentageOperationTest {

    static BankAccount bankAccount = null;
    static Deposit deposit = null;
    static InterestsMechanism interestsMechanism;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0, new InterestA());
        deposit = new Deposit(bankAccount, 100, 0, 1, new InterestA());
        interestsMechanism = new InterestA();
    }


    @Test
    public void changePercentageTest() {
        String descriptionTest = "JUnit Test";

        ChangePercentageOperation changePercentageOperationTest = new ChangePercentageOperation(deposit, interestsMechanism, descriptionTest);
        InterestsMechanism newPercentageTest = deposit.getInterestsMechanism();

        changePercentageOperationTest.execute();

        Assert.assertEquals(deposit.getInterestsMechanism(), interestsMechanism);
    }


}
