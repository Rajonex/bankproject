package operationcredit;

import interests.InterestA;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.Credit;

public class PayPercentageOperationTest {

    static BankAccount bankAccount = null;
    static Credit credit = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0, new InterestA());
        credit = new Credit(bankAccount, -100, 0, new InterestA());
    }


    @Test
    public void payPercentageTest() {
        String descriptionTest = "JUnit Test";
        PayPercentageOperation payPercentageOperationTest = new PayPercentageOperation(credit, descriptionTest);

        double balanceTest = credit.getBalance(); //stary balance
        double percentageTest = credit.getInterests(); // procent


        double payedTest = balanceTest * percentageTest; //suma o jaka powinien wzrosnac kredit
        payPercentageOperationTest.execute();
        Assert.assertEquals(balanceTest-percentageTest, credit.getBalance(), 0.01);
    }


}
