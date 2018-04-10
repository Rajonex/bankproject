package operationdeposit;

import interests.InterestsMechanism;
import operations.DepositOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.Deposit;

public class ChangePercentageOperationTest {

    static BankAccount bankAccount = null;
    static Deposit deposit = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
        deposit = new Deposit(bankAccount, 100, 0);
    }


    @Test
    public void changePercentageTest() {
        DepositOperation depositOperationTest = new DepositOperation();
        InterestsMechanism newPercentageTest = bankAccount.getInterestsMechanism();
        String descriptionTest = "JUnit Test";

        depositOperationTest.changePercentage(deposit, newPercentageTest, descriptionTest);

        Assert.assertEquals(newPercentageTest, deposit.getInterestsMechanism());

    }


}
