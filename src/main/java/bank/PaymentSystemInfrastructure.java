package bank;

import exceptions.NoSuchAccountException;
import messages.PackageToAnotherBank;
import services.IdGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentSystemInfrastructure {
    private static Map<Integer, BankImpl> bankMap = new HashMap<>();

    /**
     * sending packages to another bank
     *
     * @param listOfPackages packages we want to send
     * @return true if operation succeeded
     */
    public boolean sendPackages(List<PackageToAnotherBank> listOfPackages) {
        listOfPackages.stream().forEach(p -> {
            BankImpl bankTo = bankMap.get(p.getToBank());
            BankImpl bankFrom = bankMap.get(p.getFromBank());
            if (bankTo != null) {
                try {
                    bankTo.transferFromAnotherBank(p);
                } catch (NoSuchAccountException e) {
                    e.printStackTrace();
                }
            }
        });
        return false;
    }

    /**
     * Creating new bank with unique id
     *
     * @return new bank
     */ // TODO - Factory bank
    public BankImpl createNewBank() {
        int id = IdGenerator.generateBankId();
        BankImpl bank = new BankImpl(id);
        bankMap.put(id, bank);
        return bank;
    }
}
