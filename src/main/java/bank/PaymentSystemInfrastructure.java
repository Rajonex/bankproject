package bank;

import exceptions.NoSuchAccountException;
import messages.PackageToAnotherBank;
import services.IdGenerator;

import java.util.List;
import java.util.Map;

public class PaymentSystemInfrastructure {
    private Map<Integer, Bank> banksIdList;

    /**
     * sending packages to another bank
     *
     * @param listOfPackages packages we want to send
     * @return true if operation succeeded
     */
    public boolean sendPackages(List<PackageToAnotherBank> listOfPackages) {
        listOfPackages.stream().forEach(p -> {
            Bank bankTo = banksIdList.get(p.getToBank());
            try {
                bankTo.transferFromAnotherBank(p);
            } catch (NoSuchAccountException e) {
                e.printStackTrace();
            }
        });
        return false;
    }

    /**
     * Creating new bank with unique id
     *
     * @return new bank
     */
    public Bank createNewBank() {
        int id = IdGenerator.generateBankId();
        Bank bank = new BankA(id);
        banksIdList.put(id, bank);

        return bank;
    }
}
