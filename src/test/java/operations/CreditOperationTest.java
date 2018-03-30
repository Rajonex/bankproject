package operations;

import messages.Ack;
import messages.TypeOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.Credit;

import java.time.LocalDate;

public class CreditOperationTest {

    static BankAccount bankAccount = null;
    static Credit credit = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0, 10.0);
        credit = new Credit(bankAccount, 100, 0, 10.0);
    }

    @Test
    public void createCreditTest() {
        CreditOperation creditOperationTest = new CreditOperation();
        double balanceTest = bankAccount.getBalance();
        int ownerIdTest = bankAccount.getOwnerId();
        double percentageTest = bankAccount.getPercentage();

        String descriptionTest = "JUnit Test";

        Credit newCreditTest = creditOperationTest.createCredit(bankAccount, balanceTest, ownerIdTest, percentageTest, descriptionTest);
        Assert.assertNotNull(newCreditTest);

    }


    @Test
    public void changePercentageTest() {
        CreditOperation creditOperationTest = new CreditOperation();
        double newPercentageTest = 5.0;
        String descriptionTest = "JUnit Test";

        creditOperationTest.changePercentage(credit, newPercentageTest, descriptionTest);

        Assert.assertEquals(newPercentageTest, credit.getPercentage(), 0.0);

    }

    @Test
    public void payPercentageTest() {
        CreditOperation creditOperationTest = new CreditOperation();

        double balanceTest = credit.getBalance(); //stary balance
        double percentageTest = credit.getPercentage(); // procent
        String descriptionTest = "JUnit Test";

        double payedTest = balanceTest * percentageTest; //suma o jaka powinien wzrosnac kredit
        creditOperationTest.payPercentage(credit, descriptionTest); //wywolanie operacji na pierwotnym kredycie za posrednictwem creditOperation

        Assert.assertEquals(balanceTest, credit.getBalance(), payedTest);
    }


    @Test
    public void transferTest() {

        CreditOperation creditOperationTest = new CreditOperation();

        double balanceTest = credit.getBalance();
        String descriptionTest = "JUnit Test";
        double valueTest = 100.0;

        creditOperationTest.transfer(credit, valueTest, descriptionTest);

        Assert.assertEquals(credit.getBalance(), balanceTest, valueTest);

    }

    @Test
    public void payOfCreditTest()
    {
        CreditOperation creditOperationTest = new CreditOperation();
        String descriptionTest = "JUnit Test";
        double balanceTest = credit.getBalance();

        creditOperationTest.payOfCredit(credit, descriptionTest);

        Assert.assertEquals(balanceTest, credit.getBalance(), credit.getBalance());
    }


}
