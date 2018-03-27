package operations;

import org.junit.Assert;
import org.junit.Test;
import services.BankAccount;

public class BankAccountOperationTest {

    @Test
    public void testCreatingAccount() {
        BankAccount account = BankAccountOperation.createNormalAccount(0, 10,  "test");
        BankAccount normalAccount = new BankAccount(0, 10);
        Assert.assertEquals(normalAccount.getOwnerId(), account.getOwnerId());
    }
}