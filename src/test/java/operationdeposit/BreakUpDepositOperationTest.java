package operationdeposit;

import operations.DepositOperation;
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
        bankAccount = new BankAccount(1000, 0);
        deposit = new Deposit(bankAccount, 100, 0);
    }

    @Test
    public void breakUpDepositTest()
    {
        DepositOperation depositOperationTest = new DepositOperation();
        double balanceTest = deposit.getBalance();
        double accountBalanceTest = bankAccount.getBalance();
        String descriptionTest = "JUnit Test";

        depositOperationTest.breakUpDeposit(deposit, descriptionTest);

        Assert.assertEquals(bankAccount.getBalance(), accountBalanceTest, balanceTest);
    }
}
