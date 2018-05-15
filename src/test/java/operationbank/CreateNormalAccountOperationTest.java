package operationbank;

import messages.Ack;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;

public class CreateNormalAccountOperationTest {


    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }

    @Test
    public void createNormalAccountTest() {

        String descriptionTest = "JUnit Test";
        CreateNormalAccountOperation createNormalAccountOperationTest = new CreateNormalAccountOperation(bankAccount.getOwnerId(), descriptionTest);



        Ack newNormalAccount = createNormalAccountOperationTest.execute();
        Assert.assertNotNull(newNormalAccount);
    }
}
