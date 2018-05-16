package bank;

import exceptions.NoSuchAccountException;
import exceptions.NoSuchBankException;
import messages.PackageToAnotherBank;
import services.IdGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentSystemInfrastructure {
    private Map<Integer, Bank> banksIdList;

    public PaymentSystemInfrastructure() {
        this.banksIdList = new HashMap<>();
    }

    /**
     * sending packages to another bank
     *
     * @param listOfPackages packages we want to send
     * @return true if operation succeeded
     */
    public boolean sendPackages(List<PackageToAnotherBank> listOfPackages) {
        listOfPackages.stream().forEach(p -> {
            Bank bankTo = banksIdList.get(p.getToBank());
            if (bankTo != null) {
                try {
                    bankTo.transferFromAnotherBank(p);
                } catch (NoSuchAccountException e) {
                }
            }
            else
                try {
                    throw new NoSuchBankException("There is no bank with id=" + p.getToBank());
                } catch (NoSuchBankException e) {
                }
        });
        return false;
    }

    /**
     * Creating new bank with unique id
     *
     * @return new bank
     */
    public BankImpl createNewBank() {
        int id = IdGenerator.generateBankId();
        BankImpl bank = new BankImpl(id);
        banksIdList.put(id, bank);
        return bank;
    }
}
