package bank;


import clients.Client;
import exceptions.NoSuchAccountException;
import exceptions.NoSuchClientException;
import exceptions.NoSuchCreditException;
import exceptions.NoSuchDepositException;
import history.History;
import interests.InterestsMechanism;
import messages.*;
import operationbank.*;
import operationcredit.CreateCreditOperation;
import operationcredit.PayOfCreditOperation;
import operationcredit.TransferOperation;
import operationdeposit.BreakUpDepositOperation;
import operationdeposit.CreateDepositOperation;
import operationdeposit.SolveDepositOperation;
import reports.ReportBalance;
import reports.ReportCreateMainAccountDate;
import services.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BankImpl implements Bank {
    private static final int MAX_PACKAGE_AMOUNT = 1;
    private final int bankId;
    private PaymentSystemInfrastructure paymentSystemInfrastructure;
    private List<Client> clients;
    private List<Credit> credits;
    private List<Deposit> deposits;
    private List<Product> bankAccounts;
    private List<PackageToAnotherBank> packagesToSend;
    private History bankHistory;

    public BankImpl(int id) {
        clients = new ArrayList<>();
        credits = new ArrayList<>();
        deposits = new ArrayList<>();
        bankAccounts = new ArrayList<>();
        packagesToSend = new ArrayList<>();
        bankHistory = new History();
        paymentSystemInfrastructure = new PaymentSystemInfrastructure();
        bankId = id;
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Credit> getCredits() {
        return credits;
    }

    public List<Deposit> getDeposits() {
        return deposits;
    }

    public List<Product> getBankAccounts() {
        return bankAccounts;
    }

    public int getBankId() {
        return bankId;
    }

    /**
     * Adding new Client to the bank
     *
     * @param client cannot be null and must be unique
     * @return true if operation succeeded
     */
    @Override
    public boolean addNewClient(Client client) {
        // test if list contains client and client is not a null
        if (client != null && !getClients().stream().anyMatch(cl -> cl.getPesel().equals(client.getPesel()))) {
            // adding to list and return true if operation succeeded
            boolean ifSucceeded = clients.add(client);
            if (ifSucceeded) {
                Ack ack = new BankAck(null, null, client.getId(), TypeOperation.ADD_NEW_CLIENT, LocalDate.now(), "New client " + client + " created");
                bankHistory.add(ack);

                return true;
            }
        }

        return false;
    }

    /**
     * Checking if clients list contains client with specified id
     *
     * @param id unique id of every client
     * @return true if succeeded
     */
    private boolean ifClientExists(int id) {
        return clients.stream().filter(client -> client.getId() == id).findFirst().isPresent();
    }

    /**
     * Getting client from list by specified id
     *
     * @param id unique id of every client
     * @return
     */
    public Client getClientById(int id) throws NoSuchClientException {
        Client client = clients.stream().filter(cl -> cl.getId() == id).findAny().orElse(null);
        if (client == null)
            throw new NoSuchClientException("There is no client with id=" + id);
        return client;
    }

    /**
     * Removing Client with specified id from bank
     *
     * @param id client unique id
     * @return true if succeeded
     */
    @Override
    public boolean deleteClientById(int id) throws NoSuchClientException {
        // check if client exists
        if (ifClientExists(id)) {
            Client client = getClientById(id);
            boolean ifSucceeded = clients.removeIf(cl -> cl.getId() == id);
            if (ifSucceeded) {
                Ack ack = new BankAck(null, null, id, TypeOperation.DELETE_CLIENT, LocalDate.now(), "Client " + client + " deleted");
                bankHistory.add(ack);

                return true;
            }
        }

        return false;
    }

    // -------------------------------------------------------------------------------- BankAccount

    /**
     * Creating new normal Account for client with specified id, percentage and adding it to normalAccounts list.
     *
     * @param ownerId client's unique id
     * @return true if opeartion succeeded
     */
    @Override
    public boolean addNewNormalAccount(int ownerId) throws NoSuchClientException {
        String description = "Client " + getClientById(ownerId) + " added new account";
        BankAccount normalAccount = new BankAccount(ownerId);
        boolean ifSucceeded = bankAccounts.add(normalAccount);

        if (ifSucceeded) {
            Ack ack = new Ack(ownerId, normalAccount.getId(), TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
            bankHistory.add(ack);
            return true;
        }
        return false;
    }

    /**
     * Changing account type from normal to debet one.
     *
     * @param accountId account's unique id.
     * @param limit     limit of debet account.
     * @param debet     debet value of account
     * @return true if operation succeeded
     */
    @Override
    public boolean makeAccountDebet(int accountId, double limit, double debet) throws NoSuchClientException, NoSuchAccountException {
        BankAccount bankAccount = (BankAccount) getProductById(accountId);
        Client client = getClientById(bankAccount.getOwnerId());
        String description = "Client " + client + " account changed to debet account with " + limit + " limit";
        MakeAccountDebetOperation makeAccountDebetOperation = new MakeAccountDebetOperation(this, bankAccount, limit, description);

        Ack ack = makeAccountDebetOperation.execute();
        bankAccount.addToHistory(ack);
        bankHistory.add(ack);
        return true;

    }

    /**
     * Creating new debetAccount for client with specified id, limit, percentage and adding it to debetAccounts list.
     *
     * @param ownerId    client's unique id
     * @param limit      account's limit
     * @param percentage account's percentage
     * @return true if operation succeeded
     */
    @Override
    public boolean addNewDebetAccount(int ownerId, double limit, double debet, double percentage) throws NoSuchClientException {
        // check if ownerId is correct and if client with this id exists
        String description = "Client " + getClientById(ownerId) + " added new debet account with " + limit + " and " + percentage * 100 + " percentage";
        BankAccount bankAccount = new BankAccount(ownerId);
        DebetAccountDecorator debetAccountDecorator = new DebetAccountDecorator(limit, debet, bankAccount);
        boolean ifSucceeded = bankAccounts.add(debetAccountDecorator);

        if (ifSucceeded) {
            Ack ack = new Ack(ownerId, debetAccountDecorator.getId(), TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
            bankAccount.addToHistory(ack);
            bankAccount.addToHistory(ack);
            bankHistory.add(ack);
            return true;
        }
        return false;
    }

    /**
     * Checking if account with specified id exists
     *
     * @param accountId account id
     * @return true if account exists
     */
    private boolean ifAccountExists(int accountId) {
        return bankAccounts.stream().filter(account -> account.getId() == accountId).findFirst().isPresent();
    }

    /**
     * Checking if client's specific account exists
     *
     * @param clientId client's unique id
     * @return
     */
    private boolean ifClientAccountExists(int clientId) {
        if (ifClientExists(clientId))
            return bankAccounts.stream().filter(account -> account.getOwnerId() == clientId).findFirst().isPresent();
        else
            return false;
    }

    /**
     * Getting specified bankAccount by id
     *
     * @param accountId bankAccount's unique id
     * @return bankAccount with specified in param id
     */
    public Product getProductById(int accountId) throws NoSuchAccountException {
        Product product = bankAccounts.stream().filter(pr -> pr.getId() == accountId).findFirst().orElse(null);
        if (product == null)
            throw new NoSuchAccountException("There is no account with id=" + accountId);
        else
            return product;
    }

    // -------------------------------------------------------------------------------- Deposit

    /**
     * Creating new client's deposit. Client must have BankAccount to create new deposit.
     *
     * @param accountId  client's bankAccount's id
     * @param value      deposit's value
     * @param ownerId    client's id
     * @param duration   number of mounts we want to freeze money
     * @param percentage percentage of deposit
     * @return true if deposit created with success
     */
    @Override
    public boolean addNewDeposit(int accountId, double value, int ownerId, long duration, double percentage) throws NoSuchClientException, NoSuchAccountException {
        // check if client and its account exists
        Client client = getClientById(ownerId);
        BankAccount bankAccount = (BankAccount) getProductById(accountId);
        String description = "New " + client.getFirstName() + " " + client.getLastName() + " deposit with " + value + " and " + percentage * 100 + " created";

        CreateDepositOperation createDepositOperation = new CreateDepositOperation(bankAccount, value, ownerId, duration, description);
        Ack ack = createDepositOperation.execute();
        boolean ifSucceeded = ack != null ? true : false;

        if (ifSucceeded) {
            Deposit deposit = new Deposit(bankAccount, value, ownerId, duration);
            deposits.add(deposit);
            bankHistory.add(ack);

            return true;
        }
        return false;
    }

    // -------------------------------------------------------------------------------- Credit

    /**
     * Creating new client's credit. Client must have BankAccount to create new credit.
     *
     * @param accountId  client's bankAccount's id
     * @param balance    deposit's value
     * @param ownerId    client's id
     * @param percentage percentage of deposit
     * @return true if credit created with success
     */
    @Override
    public boolean addNewCredit(int accountId, double balance, int ownerId, double percentage) throws NoSuchClientException, NoSuchAccountException {
        Client client = getClientById(ownerId);
        BankAccount bankAccount = (BankAccount) getProductById(accountId);
        String description = "New " + client.getFirstName() + " " + client.getLastName() + " credit with " + balance + " and " + percentage * 100 + " created";

        CreateCreditOperation createCreditOperation = new CreateCreditOperation(bankAccount, balance, ownerId, description);
        Credit credit = new Credit(bankAccount, balance * (-1), ownerId);
        boolean ifSucceeded = credits.add(credit);

        if (ifSucceeded) {
            Ack ack = createCreditOperation.execute();
            bankHistory.add(ack);

            return true;
        }
        return false;
    }

    /**
     * Checking if credit with specified id exists
     *
     * @param id unique id of credit
     * @return true if credit exists
     */
    private boolean ifCreditExists(int id) {
        return credits.stream().filter(credit -> credit.getId() == id).findFirst().isPresent();
    }

    /**
     * Getting specified credit by id
     *
     * @param id credit's unique id
     * @return credit with specified in param id
     */
    private Credit getCreditById(int id) throws NoSuchCreditException {
        Credit credit = credits.stream().filter(cr -> cr.getId() == id).findAny().orElse(null);
        if (credit == null)
            throw new NoSuchCreditException("There is no credit with id=" + id);
        return credit;
    }


    // -------------------------------------------------------------------------------- BankAccountOperations

    /**
     * Transfer money from one account to another
     *
     * @param accountFromId unique id of account where money are taking from
     * @param accountToId   unique id of account where money are transferring to
     * @param value         value of transferring money between accounts
     * @return true if transfer succeeded
     */
    @Override
    public boolean transfer(int accountFromId, int accountToId, double value) throws NoSuchAccountException {
        BankAccount bankAccountFrom = (BankAccount) getProductById(accountFromId);
        BankAccount bankAccountTo = (BankAccount) getProductById(accountToId);

        String description = "money successfully transferred from account: " + bankAccountFrom + " to: " + bankAccountTo + ", with amount of " + value;
        TransferFromToOperation transferFromToOperation = new TransferFromToOperation(bankAccountFrom, bankAccountTo, value, description);

        Ack ack = transferFromToOperation.execute();
        bankHistory.add(ack);

        return true;
    }

    /**
     * Transfer money from account in another bank
     *
     * @param packageToAnotherBank package we want to send
     * @return
     */
    @Override
    public boolean transferFromAnotherBank(PackageToAnotherBank packageToAnotherBank) throws NoSuchAccountException {
        if (ifAccountExists(packageToAnotherBank.getToAccount())) {
            BankAccount bankAccount = (BankAccount) getProductById(packageToAnotherBank.getToAccount());
            if (packageToAnotherBank.getTypeOfPackage() == TypeOfPackage.NORMAL) {
                TransferInterbankOperation transferInterbankOperation = new TransferInterbankOperation(packageToAnotherBank.getFromAccount(), bankAccount, packageToAnotherBank.getValue(), "Interbank transfer");
                transferInterbankOperation.execute();
                Ack ack = new Ack(packageToAnotherBank.getFromAccount(), packageToAnotherBank.getToAccount(), TypeOperation.TRANSFER_INTERBANK, LocalDate.now(), "Interbank transfer");
                bankHistory.add(ack);
            } else {
                if (packageToAnotherBank.getTypeOfPackage() == TypeOfPackage.BOUNCED) {
                    TransferInterbankBouncedOperation transferInterbankBouncedOperation = new TransferInterbankBouncedOperation(packageToAnotherBank.getFromAccount(), bankAccount, packageToAnotherBank.getValue(), "Interbank bounced transfer");
                    transferInterbankBouncedOperation.execute();
                    Ack ack = new Ack(packageToAnotherBank.getFromAccount(), packageToAnotherBank.getToAccount(), TypeOperation.TRANSFER_BOUNCED, LocalDate.now(), "Interbank bounced transfer");
                    bankHistory.add(ack);
                }
            }
            return true;
        } else {
            PackageToAnotherBank packageToAnotherBankResponse = new PackageToAnotherBank(this.getBankId(), packageToAnotherBank.getToAccount(), packageToAnotherBank.getFromBank(), packageToAnotherBank.getFromAccount(), packageToAnotherBank.getValue(), TypeOfPackage.BOUNCED);
            sendToAntoherBank(packageToAnotherBankResponse);

            return false;
        }
    }

    /**
     * Transfering money to account in nother bank
     *
     * @param fromAccountId account from this bank
     * @param toBankId      nother bank id
     * @param toAcountId    another acount id
     * @param value         value of transferred money
     * @return true if succeeded
     */
    public boolean transferToAnotherBank(int fromAccountId, int toBankId, int toAcountId, double value) throws NoSuchAccountException {
        PackageToAnotherBank packageToAnotherBank = new PackageToAnotherBank(this.getBankId(), fromAccountId, toBankId, toAcountId, value, TypeOfPackage.NORMAL);
        return sendToAntoherBank(packageToAnotherBank);
    }

    /**
     * Sending money to another bank
     *
     * @param packageToAnotherBank package with transfer information
     * @return true if operation withdraw succeeded
     * @throws NoSuchAccountException throwing when there is no account with such id
     */
    private boolean sendToAntoherBank(PackageToAnotherBank packageToAnotherBank) throws NoSuchAccountException {
        if (packageToAnotherBank.getTypeOfPackage() == TypeOfPackage.BOUNCED || withdraw(packageToAnotherBank.getFromAccount(), packageToAnotherBank.getValue())) {
            addPackageTolist(packageToAnotherBank);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Payment money into account
     *
     * @param accountId unique id of account
     * @param value     value of transferring money
     * @return true if payment succeeded
     */
    @Override
    public boolean payment(int accountId, double value) throws NoSuchAccountException {
        BankAccount bankAccount = (BankAccount) getProductById(accountId);

        String description = "money successfully transferred to account: " + accountId + ", with amount of " + value;
        PaymentOperation paymentOperation = new PaymentOperation(bankAccount, value, description);
        Ack ack = paymentOperation.execute();
        bankAccount.addToHistory(ack);
        bankHistory.add(ack);

        return true;

    }

    /**
     * Withdraw money from account
     *
     * @param accountId unique id of account
     * @param value     value of transferring money
     * @return true if withdraw succeeded
     */
    @Override
    public boolean withdraw(int accountId, double value) throws NoSuchAccountException {
        BankAccount bankAccount = (BankAccount) getProductById(accountId);
        String description = "money successfully withdrawn from account: " + accountId + ", with amount of " + value;
        WithdrawOperation withdrawOperation = new WithdrawOperation(bankAccount, value, description);
        Ack ack = withdrawOperation.execute();
        if (ack != null) {
            bankAccount.addToHistory(ack);
            bankHistory.add(ack);

            return true;
        }

        return false;
    }

    /**
     * Removing specified by id credit from bank
     *
     * @param id unique credit's id
     * @return true if operation succeeded
     */
    public boolean deleteCreditById(int id) throws NoSuchClientException, NoSuchCreditException {
        // check if client exists
        Credit credit = getCreditById(id);
        Client client = getClientById(credit.getOwnerId());
        boolean ifSucceeded = credits.removeIf(cr -> cr.getId() == id);

        if (ifSucceeded) {
            // creating ack
            Ack ack = new BankAck(credit.getId(), null, id, TypeOperation.DELETE_CREDIT, LocalDate.now(), "Credit of id: " + id + " of client " + client + " deleted");
            bankHistory.add(ack);

            return true;
        }

        return false;
    }

    /**
     * Paying credit rate. If value is high enough credit is paying off.
     *
     * @param creditId credit unique id
     * @param value    paying value
     * @return true if operation succeeded
     */
    @Override
    public boolean payCreditRate(int creditId, double value) throws NoSuchClientException, NoSuchCreditException {
        Credit credit = getCreditById(creditId);
        String description = "rate transferred to credit's account with id " + creditId + ", value = " + value;
        TransferOperation transferOperation = new TransferOperation(credit, value, description);
        Ack ack = transferOperation.execute();
        if (ack != null) {
            bankHistory.add(ack);
            return true;
        } else {
            // credit is payed off
            Client client = getClientById(credit.getOwnerId());
            String creditPayedOffDescription = "credit of client " + client + " is payed off and can be removed";
            PayOfCreditOperation payOfCreditOperation = new PayOfCreditOperation(credit, creditPayedOffDescription);
            ack = payOfCreditOperation.execute();
            bankHistory.add(ack);
            deleteCreditById(creditId);
            return true;
        }

    }

    /**
     * Paying bank account rate.
     * TODO - Write logic!
     *
     * @param accountId bank account unique id
     * @param value     paying value
     * @return true if operation succeeded
     */
    @Override
    public boolean payBankAccountRate(int accountId, double value) {
        return false;
    }

    /**
     * Checking if deposit with specified id exists
     *
     * @param id unique id of checking deposit
     * @return true if deposit exists in bank
     */
    private boolean ifDepositExists(int id) {
        return deposits.stream().filter(deposit -> deposit.getId() == id).findFirst().isPresent();
    }

    /**
     * Getting deposit by specified id
     *
     * @param id unique deposit's id
     * @return specified deposit
     */
    private Deposit getDepositById(int id) throws NoSuchDepositException {
        Deposit deposit = deposits.stream().filter(cr -> cr.getId() == id).findAny().orElse(null);
        if (deposit == null)
            throw new NoSuchDepositException("There is no deposit with id=" + id);
        return deposit;
    }

    /**
     * Removing specified by id deposit from bank
     *
     * @param id unique deposit's id
     * @return true if operation succeeded
     */
    public boolean deleteDepositById(int id) throws NoSuchClientException, NoSuchDepositException {
        Deposit deposit = getDepositById(id);
        Client client = getClientById(deposit.getOwnerId());
        boolean ifSucceeded = deposits.removeIf(dp -> dp.getId() == id);
        if (ifSucceeded) {
            // creating ack
            Ack ack = new BankAck(deposit.getId(), null, id, TypeOperation.DELETE_DEPOSIT, LocalDate.now(), "Deposit of id: " + id + " of client " + client + " deleted");
            bankHistory.add(ack);

            return true;
        }

        return false;
    }

    /**
     * Withdrawing money from deposit. If it has expired it can be solved, otherwise
     * deposit break up.
     *
     * @param depositId deposit unique id
     * @return true if operation succeeded
     */
    @Override
    public boolean withdrawFromDeposit(int depositId) throws NoSuchClientException, NoSuchDepositException {
        Deposit deposit = getDepositById(depositId);
        Client client = getClientById(deposit.getOwnerId());
        if (deposit.isExpired()) {
            // deposit has expired
            String description = "deposit " + depositId + " of client " + client + " is solved";
            SolveDepositOperation solveDepositOperation = new SolveDepositOperation(deposit, description);
            Ack ack = solveDepositOperation.execute();
            bankHistory.add(ack);

            return true;
        } else {
            //deposit will be broken up
            String description = "deposit " + depositId + " of client " + client + " is broken up";
            BreakUpDepositOperation breakUpDepositOperation = new BreakUpDepositOperation(deposit, description);

            Ack ack = breakUpDepositOperation.execute();
            bankHistory.add(ack);

            return true;
        }

    }

    /**
     * Changing percentage of account
     *
     * @param accountId          unique account id
     * @param interestsMechanism new mechanism
     * @return true if operation succeeded
     */
    @Override
    public boolean changeAccountPercentage(int accountId, InterestsMechanism interestsMechanism) throws NoSuchAccountException {
        BankAccount bankAccount = (BankAccount) getProductById(accountId);
        InterestsMechanism newInterestMechanism = interestsMechanism;
        String description = "Amount of account (" + accountId + ") percentage changed from " + bankAccount.getInterestsMechanism() + " to " + newInterestMechanism;
        ChangePercentageOperation changePercentageOperation = new ChangePercentageOperation(bankAccount, interestsMechanism, description);

        Ack ack = changePercentageOperation.execute();
        bankHistory.add(ack);
        return true;
    }

    /**
     * Changing percentage of credit
     * TODO - Write logic!
     *
     * @param creditId      unique credit id
     * @param newPercentage new value of percentage
     * @return true if operation succeeded
     */
    @Override
    public boolean changeCreditPercentage(int creditId, double newPercentage) {
        return false;
    }

    /**
     * Changing percentage of deposit
     * TODO - Write logic!
     *
     * @param depositId     unique deposit id
     * @param newPercentage new value of percentage
     * @return true if operation succeeded
     */
    @Override
    public boolean changeDepositPercentage(int depositId, double newPercentage) {
        return false;
    }

    /**
     * Getting list of bank history
     *
     * @return bank history
     */
    @Override
    public List<Ack> getBankHistory() {
        return bankHistory.returnList();
    }

    /**
     * Adding package to packages list. If there are 5 packages it sends them.
     *
     * @param packageToAnotherBank package to send
     */
    private void addPackageTolist(PackageToAnotherBank packageToAnotherBank) {
        packagesToSend.add(packageToAnotherBank);

        if (packagesToSend.size() >= MAX_PACKAGE_AMOUNT) {
            paymentSystemInfrastructure.sendPackages(packagesToSend);
        }
    }

    // TODO - sprawdzic metode, w odpowiedni sposob obsluzyc wyjatek

    /**
     * wrapping normal account to debet one
     *
     * @param bankAccountId searched BankAccount, for which we add debet
     * @param limit         max value of debet
     * @param description   description of this action
     * @return true if operation succeeded
     */
    @Override
    public boolean wrapAccountFromNormalToDebet(int bankAccountId, double limit, String description) {
        for (int i = 0; i < bankAccounts.size(); i++) {
            if (bankAccounts.get(i).getId() == bankAccountId) {
                bankAccounts.set(i, new DebetAccountDecorator(limit, 0, bankAccounts.get(i)));
                return true;
            }
        }
        return false;
    }

    /**
     * Raporting by minimum account balance value
     *
     * @param balance minimum balance of account we are looking for
     * @return list of Products with bigger balance value
     */
    @Override
    public List<Product> getBankAccountsByBalance(double balance) {
        ReportBalance reportBalance = new ReportBalance(balance);
        for (Credit credit : credits) {
            credit.accept(reportBalance);
        }
        for (Deposit deposit : deposits) {
            deposit.accept(reportBalance);
        }
        for (Product bankAccount : bankAccounts) {
            bankAccount.accept(reportBalance);
        }
        return reportBalance.getProductsWithCriteria();
    }

    /**
     * Reporting accounts with older date of creating then we are passing
     *
     * @param date date of account creating
     * @return list of Products with older date of creating
     */
    @Override
    public List<Product> getBankAccountsByDate(LocalDate date) {
        ReportCreateMainAccountDate reportCreateMainAccountDate = new ReportCreateMainAccountDate(date);
        for (Credit credit : credits) {
            credit.accept(reportCreateMainAccountDate);
        }
        for (Deposit deposit : deposits) {
            deposit.accept(reportCreateMainAccountDate);
        }
        for (Product bankAccount : bankAccounts) {
            bankAccount.accept(reportCreateMainAccountDate);
        }
        return reportCreateMainAccountDate.getProductsWithCriteria();
    }
}