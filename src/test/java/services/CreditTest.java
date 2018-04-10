package services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CreditTest {

    Credit credit = null;

    @Before
    public void init()
    {
        BankAccount bankAccount = new BankAccount(1);
        credit = new Credit(bankAccount, -100.0, 1);
    }

    @Test
    public void increaseTestOk()
    {
        boolean result = credit.increaseBalance(20.0);
        Assert.assertTrue(result);
        Assert.assertEquals(-80.0, credit.getBalance(), 0.01);
    }

    @Test
    public void increaseTestNotOk()
    {
        boolean result = credit.increaseBalance(120.0);
        Assert.assertFalse(result);
        Assert.assertEquals(-100.0, credit.getBalance(), 0.01);
    }

    @Test
    public void decreaseTest()
    {
        boolean result = credit.decreaseBalance(100.0);
        Assert.assertTrue(result);
        Assert.assertEquals(-200.0, credit.getBalance(), 0.01);
    }

    @After
    public void finish()
    {
        credit = null;
    }

}
