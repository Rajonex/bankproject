package interests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.BankAccount;

public class InterestBTest {

    BankAccount bankAccount = null;
    InterestsMechanism interestsMechanism = null;
    @Before
    public void init()
    {
        interestsMechanism = new InterestB();
    }

    @Test
    public void firstLimit()
    {
        double value = 1000.0;
        bankAccount = new BankAccount(value, 1, new InterestA());
        bankAccount.setInterestsMechanism(interestsMechanism);
        Assert.assertEquals(value * 0.03, bankAccount.getInterests(), 0.01);
    }

    @Test
    public void secondLimit()
    {
        double value = 10000.0;
        bankAccount = new BankAccount(value, 1, new InterestA());
        bankAccount.setInterestsMechanism(interestsMechanism);
        Assert.assertEquals(value * 0.05, bankAccount.getInterests(), 0.01);
    }

    @Test
    public void thirdLimit()
    {
        double value = 70000.0;
        bankAccount = new BankAccount(value, 1, new InterestA());
        bankAccount.setInterestsMechanism(interestsMechanism);
        Assert.assertEquals(value * 0.1, bankAccount.getInterests(), 0.1);
    }

    @Test
    public void fourthLimit()
    {
        double value = 200000.0;
        bankAccount = new BankAccount(value, 1, new InterestA());
        bankAccount.setInterestsMechanism(interestsMechanism);
        Assert.assertEquals(value * 0.12, bankAccount.getInterests(), 0.1);
    }

    @After
    public void finish()
    {
        interestsMechanism = null;
    }
}
