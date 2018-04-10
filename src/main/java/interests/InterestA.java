package interests;

import services.Service;

public class InterestA implements InterestsMechanism {
    @Override
    public double interests(Service service) {
        return service.getBalance() * 0.03;
    }
}
