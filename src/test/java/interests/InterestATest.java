package interests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.NormalAccount;

public class InterestATest {

    NormalAccount normalAccount = null;
    InterestsMechanism interestsMechanism = null;
    @Before
    public void init()
    {
        interestsMechanism = new InterestA();
        normalAccount = new NormalAccount(100.0, 1);
        normalAccount.setInterestsMechanism(interestsMechanism);
    }

    @Test
    public void test()
    {
        Assert.assertEquals(3.0, normalAccount.getInterests(), 0.01);
    }

    @After
    public void finish()
    {
        interestsMechanism = null;
        normalAccount = null;
    }
}
