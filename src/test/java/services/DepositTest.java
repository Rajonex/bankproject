package services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DepositTest {

    Deposit deposit = null;

    @Before
    public void init()
    {
        BankAccount bankAccount = new BankAccount(1 );
        deposit = new Deposit(bankAccount, 100.0 , 1);
    }

    @Test
    public void decreaseTest()
    {
        boolean result = deposit.decreaseBalance(120.0);
        Assert.assertFalse(result);
        Assert.assertEquals(100.0, deposit.getBalance(), 0.01);
    }

    @After
    public void finish()
    {
        deposit = null;
    }
}
