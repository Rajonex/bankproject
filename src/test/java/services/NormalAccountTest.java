package services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NormalAccountTest {

    NormalAccount normalAccount = null;

    @Before
    public void init() {
        normalAccount = new NormalAccount(100.0, 1, 1.0);
    }

    @Test
    public void decreaseTest() {
        boolean result = normalAccount.decreaseBalance(120.0);
        Assert.assertFalse(result);
        Assert.assertEquals(100.0, normalAccount.getBalance(), 0.01);
    }

    @After
    public void finish()
    {
        normalAccount = null;
    }
}
