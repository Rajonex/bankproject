package services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DebetAccountTest {


    DebetAccount debetAccount = null;
    @Before
    public void init()
    {
        debetAccount = new DebetAccount(100.0, 1, -300.0);
    }

    @Test
    public void decreaseTestOk()
    {
        boolean result = debetAccount.decreaseBalance(200.0);
        Assert.assertTrue(result);
        Assert.assertEquals(-100.0, debetAccount.getBalance(), 0.01);
    }

    @Test
    public void decreaseTestNotOk()
    {
        boolean result = debetAccount.decreaseBalance(500.0);
        Assert.assertFalse(result);
        Assert.assertEquals(100.0, debetAccount.getBalance(), 0.01);
    }

    @After
    public void finish()
    {
        debetAccount = null;
    }
}
