package interests;

import services.Service;

public class InterestB implements InterestsMechanism {
    @Override
    public double interests(Service service) {
        if(service.getBalance() < 5000)
        {
            return service.getBalance() * 0.03;
        }
        else if(service.getBalance() < 50_000)
        {
            return service.getBalance() * 0.05;
        }
        else if(service.getBalance() < 100_000)
        {
            return service.getBalance() * 0.10;
        }
        else
            return service.getBalance() * 0.12;
    }
}
