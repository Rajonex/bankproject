package operationdeposit;

import operations.DepositOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.Deposit;

public class SolveDepositOperationTest {

    static BankAccount bankAccount = null;
    static Deposit deposit = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
        deposit = new Deposit(bankAccount, 100, 0);
    }


    @Test
    public void solveDepositTest()
    {
        DepositOperation depositOperationTest = new DepositOperation();
        double percentageTest = deposit.getInterests();
        double balanceTest = deposit.getBalance();
        double accountBalanceTest = bankAccount.getBalance();
        String descriptionTest = "JUnit Test";

        double paymentTest = percentageTest*balanceTest+balanceTest;

        depositOperationTest.solveDeposit(deposit, descriptionTest);

        Assert.assertEquals(bankAccount.getBalance(), accountBalanceTest, paymentTest);
    }
}
