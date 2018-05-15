package operationbank;

import messages.Ack;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.DebetAccountDecorator;

public class CreateDebetAccountDecoratorOperationTest {

    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }


    @Test
    public void createDebetAccountTest() {
        String descriptionTest = "JUnit Test";
        double limitTest = 1000.0;
        double valueTest = 500.0;
        CreateDebetAccountOperation createDebetAccountOperationTest = new CreateDebetAccountOperation(bankAccount, limitTest, valueTest, descriptionTest);

        Ack newDebetAccountDecorator = createDebetAccountOperationTest.execute();
        Assert.assertNotNull(newDebetAccountDecorator);
    }

}
