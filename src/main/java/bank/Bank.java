package bank;

import clients.Client;
import exceptions.NoSuchAccountException;
import exceptions.NoSuchClientException;
import exceptions.NoSuchCreditException;
import exceptions.NoSuchDepositException;
import interests.InterestsMechanism;
import messages.Ack;
import messages.PackageToAnotherBank;
import services.Product;

import java.time.LocalDate;
import java.util.List;

public interface Bank {
    boolean addNewClient(Client client);

    boolean deleteClientById(int id) throws NoSuchClientException;

    boolean addNewNormalAccount(int ownerId) throws NoSuchClientException;

    boolean addNewDebetAccount(int ownerId, double limit, double debet, InterestsMechanism interestsMechanism) throws NoSuchClientException;

    boolean makeAccountDebet(int accountId, double limit, double debet) throws NoSuchClientException, NoSuchAccountException;

    boolean addNewDeposit(int accountId, double value, int ownerId, long duration, InterestsMechanism interestsMechanism) throws NoSuchClientException, NoSuchAccountException;

    boolean addNewCredit(int accountId, double balance, int ownerId, InterestsMechanism interestsMechanism) throws NoSuchClientException, NoSuchAccountException;

    boolean transfer(int accountFromId, int accountToId, double value) throws NoSuchAccountException;

    boolean transferFromAnotherBank(PackageToAnotherBank packageToAnotherBank) throws NoSuchAccountException;

    boolean payment(int accountId, double value) throws NoSuchAccountException;

    boolean withdraw(int accountId, double value) throws NoSuchAccountException;

    boolean payCreditRate(int creditId, double value) throws NoSuchClientException, NoSuchCreditException;

    boolean withdrawFromDeposit(int depositId) throws NoSuchClientException, NoSuchDepositException;

    boolean changeAccountPercentage(int accountId, InterestsMechanism interestsMechanism) throws NoSuchAccountException;

    boolean changeCreditPercentage(int creditId, InterestsMechanism interestsMechanism) throws NoSuchCreditException;

    boolean changeDepositPercentage(int depositId, InterestsMechanism interestsMechanism) throws NoSuchDepositException;

    List<Ack> getBankHistory();

    boolean wrapAccountFromNormalToDebet(int bankAccountId, double limit, String description);

    List<Product> getBankAccountsByBalance(double balance);

    List<Product> getBankAccountsByDate(LocalDate date);
}
