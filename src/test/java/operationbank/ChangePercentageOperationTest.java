package operationbank;

import interests.InterestsMechanism;
import operations.BankAccountOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;

public class ChangePercentageOperationTest {

    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }


    @Test
    public void changePercentageTest() {
        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();

        InterestsMechanism newPercentageTest = bankAccount.getInterestsMechanism();
        String descriptionTest = "JUnit Test";

        bankAccountOperationTest.changePercentage(bankAccount, newPercentageTest, descriptionTest);

        Assert.assertEquals(newPercentageTest, bankAccount.getInterestsMechanism());

    }
}
