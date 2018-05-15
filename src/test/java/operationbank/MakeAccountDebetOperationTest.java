//package operationbank;
//
//import messages.Ack;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import services.BankAccount;
//import services.DebetAccountDecorator;
//import services.Product;
//
//public class MakeAccountDebetOperationTest {
//
//    static Product bankAccount = null;
//
//    @BeforeClass
//    static public void newBankAccountTest() {
//        bankAccount = new BankAccount(1000, 0);
//    }
//
//    @Test
//    public void makeAccountDebetTest()
//    {
//
//        String descriptionTest = "JUnit Test";
//        MakeAccountDebetOperation makeAccountDebetOperationTest = new MakeAccountDebetOperation(bankAccount, 500, descriptionTest);
//        System.out.println(bankAccount);
//        makeAccountDebetOperationTest.execute();
//        System.out.println(bankAccount);
//        boolean result = bankAccount.decreaseBalance(1200);
//        Assert.assertTrue(bankAccount instanceof DebetAccountDecorator);
////        Assert.assertEquals(bankAccount.getBalance(), -200, 0.1);
//
////        Assert.assertTrue(result);
//    }
//}
