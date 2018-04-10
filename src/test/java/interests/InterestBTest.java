package interests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.NormalAccount;

public class InterestBTest {

    NormalAccount normalAccount = null;
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
        normalAccount = new NormalAccount(value, 1);
        normalAccount.setInterestsMechanism(interestsMechanism);
        Assert.assertEquals(value * 0.03, normalAccount.getInterests(), 0.01);
    }

    @Test
    public void secondLimit()
    {
        double value = 10000.0;
        normalAccount = new NormalAccount(value, 1);
        normalAccount.setInterestsMechanism(interestsMechanism);
        Assert.assertEquals(value * 0.05, normalAccount.getInterests(), 0.01);
    }

    @Test
    public void thirdLimit()
    {
        double value = 70000.0;
        normalAccount = new NormalAccount(value, 1);
        normalAccount.setInterestsMechanism(interestsMechanism);
        Assert.assertEquals(value * 0.1, normalAccount.getInterests(), 0.1);
    }

    @Test
    public void fourthLimit()
    {
        double value = 200000.0;
        normalAccount = new NormalAccount(value, 1);
        normalAccount.setInterestsMechanism(interestsMechanism);
        Assert.assertEquals(value * 0.12, normalAccount.getInterests(), 0.1);
    }

    @After
    public void finish()
    {
        interestsMechanism = null;
    }
}
