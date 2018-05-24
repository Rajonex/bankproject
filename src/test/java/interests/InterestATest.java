package interests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.BankAccount;

public class InterestATest {

    BankAccount bankAccount = null;
    InterestsMechanism interestsMechanism = null;
    @Before
    public void init()
    {
        interestsMechanism = new InterestA();
        bankAccount = new BankAccount(100.0, 1, new InterestA());
        bankAccount.setInterestsMechanism(interestsMechanism);
    }

    @Test
    public void test()
    {
        Assert.assertEquals(3.0, bankAccount.getInterests(), 0.01);
    }

    @After
    public void finish()
    {
        interestsMechanism = null;
        bankAccount = null;
    }
}