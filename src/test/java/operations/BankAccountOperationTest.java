package operations;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.BankAccount;
import services.DebetAccount;
import services.NormalAccount;

public class BankAccountOperationTest {

    static BankAccount bankAccount = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0, 10.0);
    }


    @Test
    public void createDebetAccountTest() {
        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        int ownerIdTest = bankAccount.getOwnerId();
        double limitTest = 1000.0;
        double percentageTest = bankAccount.getPercentage();
        String descriptionTest = "JUnit Test";

        DebetAccount newDebetAccount = bankAccountOperationTest.createDebetAccount(ownerIdTest, limitTest, percentageTest, descriptionTest);
        Assert.assertNotNull(newDebetAccount);
    }


    @Test
    public void createNormalAccountTest() {
        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        int ownerIdTest = bankAccount.getOwnerId();
        double limitTest = 1000.0;
        double percentageTest = bankAccount.getPercentage();
        String descriptionTest = "JUnit Test";

        NormalAccount newNormalAccount = bankAccountOperationTest.createNormalAccount(ownerIdTest, percentageTest, descriptionTest);
        Assert.assertNotNull(newNormalAccount);
    }

    @Test
    public void makeAccountDebetTest()
    {

//        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
//        double limitTest = 1000.0;
//        String descriptionTest = "JUnit Test";
//
//        BankAccount bankAccount1Test = bankAccount;
//        bankAccount1Test = new DebetAccount(bankAccount1Test, limitTest);
//
//        bankAccountOperationTest.makeAccountDebet(bankAccount, limitTest, descriptionTest);
//
//        Assert.assertEquals(bankAccount, bankAccount1Test);

        double limitTest = 1000.0;
        String descriptionTest = "JUnit Test";

        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        boolean isCreated = bankAccountOperationTest.makeAccountDebet(bankAccount, limitTest, descriptionTest);
        Assert.assertTrue(isCreated);
    }

    @Test
    public void makeAccountNormalTest()
    {

//        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
//        double limitTest = 1000.0;
//        String descriptionTest = "JUnit Test";
//
//        BankAccount bankAccount1Test = bankAccount;
//        bankAccount1Test = new NormalAccount(bankAccount1Test);
//
//        bankAccountOperationTest.makeAccountNormal(bankAccount, descriptionTest);
//
//        Assert.assertEquals(bankAccount, bankAccount1Test);


        String descriptionTest = "JUnit Test";

        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        boolean isCreated = bankAccountOperationTest.makeAccountNormal(bankAccount, descriptionTest);
        Assert.assertTrue(isCreated);

    }

    @Test
    public void testCreatingAccount() {
        BankAccount account = BankAccountOperation.createNormalAccount(0, 10,  "test");
        BankAccount normalAccount = new BankAccount(0, 10);
        Assert.assertEquals(normalAccount.getOwnerId(), account.getOwnerId());
    }


    @Test
    public void paymentTest()
    {
        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        double paymentTest = 100.0;
        String descriptionTest = "JUnit Test";
        double balanceTest = bankAccount.getBalance();

        bankAccountOperationTest.payment(bankAccount, paymentTest, descriptionTest);

        Assert.assertEquals(balanceTest+paymentTest, bankAccount.getBalance(), 0.0);
    }

    @Test
    public void withdrawTest()
    {
        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        double paymentTest = 100.0;
        String descriptionTest = "JUnit Test";
        double balanceTest = bankAccount.getBalance();

        bankAccountOperationTest.withdraw(bankAccount, paymentTest, descriptionTest);

        Assert.assertEquals(balanceTest-paymentTest, bankAccount.getBalance(), 0.0);
    }


    @Test
    public void transferFromToTest()
    {
        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        BankAccount bankAccountCopyTest = bankAccount;
        double valueTest = 100;
        String descriptionTest = "JUnit Test";

        bankAccountOperationTest.transferFromTo(bankAccount, bankAccountCopyTest, valueTest, descriptionTest);

        Assert.assertEquals(bankAccount.getBalance(), bankAccountCopyTest.getBalance(), valueTest*2);

    }

    @Test
    public void payPercentageTest() {
        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();

        double balanceTest = bankAccount.getBalance(); //stary balance
        double percentageTest = bankAccount.getPercentage(); // procent
        String descriptionTest = "JUnit Test";
        double payedTest = balanceTest * percentageTest; //suma o jaka powinien wzrosnac kredit

        bankAccountOperationTest.payPercentage(bankAccount, descriptionTest); //wywolanie operacji na pierwotnym kredycie za posrednictwem creditOperation

        Assert.assertEquals(balanceTest, bankAccount.getBalance(), payedTest);
    }

    @Test
    public void changePercentageTest() {
        BankAccountOperation bankAccountOperationTest = new BankAccountOperation();
        double newPercentageTest = 5.0;
        String descriptionTest = "JUnit Test";

        bankAccountOperationTest.changePercentage(bankAccount, newPercentageTest, descriptionTest);

        Assert.assertEquals(newPercentageTest, bankAccount.getPercentage(), 0.0);

    }




}