package interests;

import services.Service;

public class InterestC implements InterestsMechanism {
    @Override
    public double interests(Service service) {
        double balance = service.getBalance();
        double percentage;
        if (balance < 500_000) {
            percentage = 0.13 * balance / 500_000;
        } else {
            percentage = 0.13;
        }
        return balance * percentage;
    }
}
