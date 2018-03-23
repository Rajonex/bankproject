package operations;

import history.History;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.BankAccount;

public class BankAccountOperationTest {
    private History history;

    @Before
    public void setUp() {
        history = new History();
    }

    @Test
    public void testCreatingAccount() {
        BankAccount account = BankAccountOperation.createNormalAccount(0, 10, history, "test");
        BankAccount normalAccount = new BankAccount(0, 10);
        Assert.assertEquals(normalAccount.getOwnerId(), account.getOwnerId());
    }
}