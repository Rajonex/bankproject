package operationbank;

import operations.BankAccountOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.DebetAccount;

public class CreateDebetAccountOperationTest {

    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }


    @Test
    public void createDebetAccountTest() {
        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        int ownerIdTest = bankAccount.getOwnerId();
        double limitTest = 1000.0;
        String descriptionTest = "JUnit Test";

        DebetAccount newDebetAccount = bankAccountOperationTest.createDebetAccount(ownerIdTest, limitTest, descriptionTest);
        Assert.assertNotNull(newDebetAccount);
    }

}
