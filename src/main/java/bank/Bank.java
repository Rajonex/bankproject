package bank;

import clients.Client;
import interests.InterestsMechanism;
import messages.Ack;
import messages.PackageToAnotherBank;

import java.util.List;

public interface Bank {
    boolean addNewClient(Client client);

    boolean deleteClientById(int id);

    boolean addNewNormalAccount(int ownerId, double percentage);

    boolean makeAccountDebet(int accountId, double limit);

    boolean addNewDebetAccount(int ownerId, double limit, double debet, double percentage);

    boolean makeAccountNormal(int accountId);

    boolean addNewDeposit(int accountId, double value, int ownerId, double percentage);

    boolean addNewCredit(int accountId, double balance, int ownerId, double percentage);

    boolean transfer(int accountFromId, int accountToId, double value);

    boolean transferFromAnotherBank(PackageToAnotherBank packageToAnotherBank);

    boolean payment(int accountId, double value);

    boolean withdraw(int accountId, double value);

    boolean payCreditRate(int creditId, double value);

    boolean payBankAccountRate(int accountId, double value);

    boolean withdrawFromDeposit(int depositId);

    boolean changeAccountPercentage(int accountId, InterestsMechanism interestsMechanism);

    boolean changeCreditPercentage(int creditId, double newPercentage);

    boolean changeDepositPercentage(int depositId, double newPercentage);

    List<Ack> getBankHistory();
}
