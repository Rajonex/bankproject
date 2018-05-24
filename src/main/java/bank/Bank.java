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

// TODO Implement class implements Report - filter all objects type Product by method visit of Report object (use methode accept(Report report) on all objects Product) and after return Products according the criteria with method report.getProductsWithCriteria()

public interface Bank {
    boolean addNewClient(Client client);

    boolean deleteClientById(int id) throws NoSuchClientException;

    boolean addNewNormalAccount(int ownerId) throws NoSuchClientException;

//    boolean addNewNormalAccount(int ownerId, double balance, double percentage);

    boolean addNewDebetAccount(int ownerId, double limit, double debet, double percentage) throws NoSuchClientException;

//    boolean makeAccountNormal(int accountId) throws NoSuchClientException, NoSuchAccountException;

    boolean makeAccountDebet(int accountId, double limit, double debet) throws NoSuchClientException, NoSuchAccountException;

    boolean addNewDeposit(int accountId, double value, int ownerId, long duration, double percentage) throws NoSuchClientException, NoSuchAccountException;

    boolean addNewCredit(int accountId, double balance, int ownerId, double percentage) throws NoSuchClientException, NoSuchAccountException;

    boolean transfer(int accountFromId, int accountToId, double value) throws NoSuchAccountException;

    boolean transferFromAnotherBank(PackageToAnotherBank packageToAnotherBank) throws NoSuchAccountException;

    boolean payment(int accountId, double value) throws NoSuchAccountException;

    boolean withdraw(int accountId, double value) throws NoSuchAccountException;

    boolean payCreditRate(int creditId, double value) throws NoSuchClientException, NoSuchCreditException;

    boolean payBankAccountRate(int accountId, double value);

    boolean withdrawFromDeposit(int depositId) throws NoSuchClientException, NoSuchDepositException;

    boolean changeAccountPercentage(int accountId, InterestsMechanism interestsMechanism) throws NoSuchAccountException;

    boolean changeCreditPercentage(int creditId, double newPercentage);

    boolean changeDepositPercentage(int depositId, double newPercentage);

    List<Ack> getBankHistory();

    boolean wrapAccountFromNormalToDebet(int bankAccountId, double limit, String description);

    List<Product> getBankAccountsByBalance(double balance);

    List<Product> getBankAccountsByDate(LocalDate date);
}
