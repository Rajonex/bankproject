package interests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.NormalAccount;

public class InterestCTest {

    NormalAccount normalAccount = null;
    InterestsMechanism interestsMechanism = null;
    @Before
    public void init()
    {
        interestsMechanism = new InterestC();
    }

    @Test
    public void firstLimit()
    {
        double value = 100000.0;
        normalAccount = new NormalAccount(value, 1);
        normalAccount.setInterestsMechanism(interestsMechanism);
        Assert.assertEquals((0.13 * value / 500_000) * value, normalAccount.getInterests(), 0.1);
    }

    @Test
    public void secondLimit()
    {
        double value = 1000000.0;
        normalAccount = new NormalAccount(value, 1);
        normalAccount.setInterestsMechanism(interestsMechanism);
        Assert.assertEquals(0.13 * value, normalAccount.getInterests(), 0.1);
    }

    @After
    public void finish()
    {
        interestsMechanism = null;
    }
}
