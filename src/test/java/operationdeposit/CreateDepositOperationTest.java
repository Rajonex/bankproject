package operationdeposit;

import operations.DepositOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.Deposit;

public class CreateDepositOperationTest {

    static BankAccount bankAccount = null;
    static Deposit deposit = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
        deposit = new Deposit(bankAccount, 100, 0);
    }



    @Test
    public void createDepositTest() {
        DepositOperation depositOperationTest = new DepositOperation();
        double balanceTest = bankAccount.getBalance();
        int ownerIdTest = bankAccount.getOwnerId();

        String descriptionTest = "JUnit Test";

        Deposit newDepositTest = depositOperationTest.createDeposit(bankAccount, balanceTest, ownerIdTest, descriptionTest);
        Assert.assertNotNull(newDepositTest);

    }
}
