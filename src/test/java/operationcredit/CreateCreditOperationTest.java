package operationcredit;

import org.junit.BeforeClass;
import services.BankAccount;
import services.Credit;

public class CreateCreditOperationTest {

    static BankAccount bankAccount = null;
    static Credit credit = null;

    @BeforeClass
    static public void newBankAccountTest() {
        bankAccount = new BankAccount(1000, 0);
        credit = new Credit(bankAccount, 100, 0);
    }


//    @Test
//    public void createCreditTest() {
//        CreditOperation creditOperationTest = new CreditOperation();
//        double balanceTest = bankAccount.getBalance();
//        int ownerIdTest = bankAccount.getOwnerId();
//
//        String descriptionTest = "JUnit Test";
//
//        Credit newCreditTest = creditOperationTest.createCredit(bankAccount, balanceTest, ownerIdTest, descriptionTest);
//        Assert.assertNotNull(newCreditTest);
//
//    }
}
