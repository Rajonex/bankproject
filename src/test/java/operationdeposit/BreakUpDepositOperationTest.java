package operationdeposit;

import interests.InterestA;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.Deposit;

public class BreakUpDepositOperationTest {

    static BankAccount bankAccount = null;
    static Deposit deposit = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0, new InterestA());
        deposit = new Deposit(bankAccount, 100, 0, 1, new InterestA());
    }

    @Test
    public void breakUpDepositTest() {
        String descriptionTest = "JUnit Test";

        BreakUpDepositOperation breakUpDepositOperationTest = new BreakUpDepositOperation(deposit, descriptionTest);
        double balanceTest = deposit.getBalance();
        double accountBalanceTest = bankAccount.getBalance();

        breakUpDepositOperationTest.execute();

        Assert.assertEquals(bankAccount.getBalance(), accountBalanceTest+balanceTest, 0.01);
    }
}
