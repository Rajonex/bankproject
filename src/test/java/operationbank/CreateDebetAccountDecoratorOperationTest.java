package operationbank;

import org.junit.BeforeClass;
import services.BankAccount;

public class CreateDebetAccountDecoratorOperationTest {

    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }


//    @Test
//    public void createDebetAccountTest() {
//        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
//        int ownerIdTest = bankAccount.getOwnerId();
//        double limitTest = 1000.0;
//        String descriptionTest = "JUnit Test";
//
//        DebetAccountDecorator newDebetAccountDecorator = bankAccountOperationTest.createDebetAccount(ownerIdTest, limitTest, descriptionTest);
//        Assert.assertNotNull(newDebetAccountDecorator);
//    }

}
