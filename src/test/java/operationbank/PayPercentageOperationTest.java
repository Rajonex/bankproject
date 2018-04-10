package operationbank;

import operations.BankAccountOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;

public class PayPercentageOperationTest {

    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
    }



    @Test
    public void payPercentageTest() {
        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();

        double balanceTest = bankAccount.getBalance(); //stary balance
        double percentageTest = bankAccount.getInterests(); // procent
        String descriptionTest = "JUnit Test";
        double payedTest = percentageTest; //suma o jaka powinien wzrosnac kredit

        bankAccountOperationTest.payPercentage(bankAccount, descriptionTest); //wywolanie operacji na pierwotnym kredycie za posrednictwem creditOperation

        Assert.assertEquals(balanceTest, bankAccount.getBalance(), payedTest);
    }
}
