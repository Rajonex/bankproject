package operationbank;

import operations.BankAccountOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.NormalAccount;

public class CreateNormalAccountOperationTest {


    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }

    @Test
    public void createNormalAccountTest() {
        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        int ownerIdTest = bankAccount.getOwnerId();
        double limitTest = 1000.0;
        String descriptionTest = "JUnit Test";

        NormalAccount newNormalAccount = bankAccountOperationTest.createNormalAccount(ownerIdTest, descriptionTest);
        Assert.assertNotNull(newNormalAccount);
    }
}
