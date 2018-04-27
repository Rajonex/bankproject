package bank;

import messages.PackageToAnotherBank;

import java.util.List;
import java.util.Map;

public class PaymentSystemInfrastructure {
    private Map<Integer, Bank> banksIdList;

    public boolean sendPackages(List<PackageToAnotherBank> listOfPackages) {
        listOfPackages.stream().forEach(p -> {
            Bank bankTo = banksIdList.get(p.getToBank());
            bankTo.transferFromAnotherBank(p);
        });
        return false;
    }
}
