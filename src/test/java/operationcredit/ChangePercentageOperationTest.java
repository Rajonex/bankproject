package operationcredit;

import interests.InterestsMechanism;
import operations.CreditOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.Credit;

public class ChangePercentageOperationTest {

    static BankAccount bankAccount = null;
    static Credit credit = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
        credit = new Credit(bankAccount, 100, 0);
    }


    @Test
    public void changePercentageTest() {
        CreditOperation creditOperationTest = new CreditOperation();
        InterestsMechanism newPercentageTest = bankAccount.getInterestsMechanism();
        String descriptionTest = "JUnit Test";

        creditOperationTest.changePercentage(credit, newPercentageTest, descriptionTest);

        Assert.assertEquals(newPercentageTest, credit.getInterestsMechanism());

    }

}
