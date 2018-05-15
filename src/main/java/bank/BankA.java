package bank;


import clients.Client;
import history.History;
import interests.InterestsMechanism;
import messages.*;
import operationbank.*;
import operationcredit.CreateCreditOperation;
import operationcredit.PayOfCreditOperation;
import operationcredit.PayPercentageOperation;
import operationdeposit.BreakUpDepositOperation;
import operationdeposit.CreateDepositOperation;
import operationdeposit.SolveDepositOperation;
import services.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BankA implements Bank {
    private static final int MAX_PACKAGE_AMOUNT = 5;
    private final int bankId;
    private PaymentSystemInfrastructure paymentSystemInfrastructure;
    private List<Client> clients;
    private List<Credit> credits;
    private List<Deposit> deposits;
    private List<Product> bankAccounts;
    private List<PackageToAnotherBank> packagesToSend;
    private History bankHistory;

    public BankA(int id) {
        clients = new ArrayList<>();
        credits = new ArrayList<>();
        deposits = new ArrayList<>();
        bankAccounts = new ArrayList<>();
        packagesToSend = new ArrayList<>();
        bankHistory = new History();
        paymentSystemInfrastructure = new PaymentSystemInfrastructure();
        bankId = id;
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
        if (client != null && !(clients.contains(client))) {
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
    private Client getClientById(int id) {
        return clients.stream().filter(cl -> cl.getId() == id).findFirst().get();
    }

    /**
     * Removing Client with specified id from bank
     *
     * @param id client unique id
     * @return true if succeeded
     */
    @Override
    public boolean deleteClientById(int id) {
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
     * @param ownerId    client's unique id
     * @param percentage account's percentage
     * @return true if opeartion succeeded
     */
    @Override
    public boolean addNewNormalAccount(int ownerId, double percentage) {
        // check if ownerId is correct and if client with this id exists
        if (ownerId >= 0 && ifClientExists(ownerId)) {
            String description = "Client " + getClientById(ownerId) + " added new account with " + percentage * 100 + " percentage";
            CreateNormalAccountOperation createNormalAccountOperation = new CreateNormalAccountOperation(ownerId, description);
            Ack ack = createNormalAccountOperation.execute();
            NormalAccount normalAccount = new NormalAccount(ownerId);
            boolean ifSucceeded = bankAccounts.add(normalAccount);

            if (ifSucceeded) {
                bankHistory.add(ack);
                return true;
            }
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
    public boolean makeAccountDebet(int accountId, double limit, double debet) {
        if (ifAccountExists(accountId)) {
            BankAccount bankAccount = getBankAccountById(accountId);
            Client client = getClientById(bankAccount.getOwnerId());
            String description = "Client " + client + " account changed to debet account with " + limit + " limit";
            MakeAccountDebetOperation makeAccountDebetOperation = new MakeAccountDebetOperation(bankAccount, limit, debet, description);

            Ack ack = makeAccountDebetOperation.execute();
            bankAccount.addToHistory(ack);
            bankHistory.add(ack);
            return true;
        }

        return false;
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
    public boolean addNewDebetAccount(int ownerId, double limit, double debet, double percentage) {
        // check if ownerId is correct and if client with this id exists
        if (ownerId >= 0 && ifClientExists(ownerId)) {
            String description = "Client " + getClientById(ownerId) + " added new debet account with " + limit + " and " + percentage * 100 + " percentage";
            BankAccount bankAccount = new BankAccount(ownerId);
            DebetAccountDecorator debetAccountDecorator = new DebetAccountDecorator(limit, debet, bankAccount);
            CreateDebetAccountOperation createDebetAccountOperation = new CreateDebetAccountOperation(bankAccount, debet, limit, description);
            boolean ifSucceeded = bankAccounts.add(debetAccountDecorator);

            if (ifSucceeded) {
                Ack ack = createDebetAccountOperation.execute();
                bankAccount.addToHistory(ack);
                bankAccount.addToHistory(ack);
                bankHistory.add(ack);
                return true;
            }
        }

        return false;
    }

    /**
     * Changing account type from debet to normal one.
     *
     * @param accountId account's unique id
     * @return true if operation succeeded
     */
    @Override
    public boolean makeAccountNormal(int accountId) {
        if (ifAccountExists(accountId)) {
            BankAccount bankAccount = getBankAccountById(accountId);
            Client client = getClientById(bankAccount.getOwnerId());
            String description = "Client " + client + " account changed to normal account";
            MakeAccountNormalOperation makeAccountNormalOperation = new MakeAccountNormalOperation(bankAccount, description);

            Ack ack = makeAccountNormalOperation.execute();
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
    private BankAccount getBankAccountById(int accountId) {
        return (BankAccount) bankAccounts.stream().filter(pr -> pr.getId() == accountId).findFirst().get();
    }

    // -------------------------------------------------------------------------------- Deposit

    /**
     * Creating new client's deposit. Client must have BankAccount to create new deposit.
     *
     * @param accountId  client's bankAccount's id
     * @param value      deposit's value
     * @param ownerId    client's id
     * @param percentage percentage of deposit
     * @return true if deposit created with success
     */
    @Override
    public boolean addNewDeposit(int accountId, double value, int ownerId, double percentage) {
        // check if client and its account exists
        if (ifClientAccountExists(ownerId)) {
            Client client = getClientById(ownerId);
            BankAccount bankAccount = getBankAccountById(accountId);
            String description = "New " + client.getFirstName() + " " + client.getLastName() + " deposit with " + value + " and " + percentage * 100 + " created";

            CreateDepositOperation createDepositOperation = new CreateDepositOperation(bankAccount, value, ownerId, description);

            Deposit deposit = new Deposit(bankAccount, value, ownerId);
            boolean ifSucceeded = deposits.add(deposit);

            if (ifSucceeded) {
                Ack ack = createDepositOperation.execute();
                bankHistory.add(ack);

                return true;
            }
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
    public boolean addNewCredit(int accountId, double balance, int ownerId, double percentage) {
        // check if client and its account exists
        if (ifClientAccountExists(ownerId)) {
            Client client = getClientById(ownerId);
            BankAccount bankAccount = getBankAccountById(accountId);
            String description = "New " + client.getFirstName() + " " + client.getLastName() + " credit with " + balance + " and " + percentage * 100 + " created";

            CreateCreditOperation createCreditOperation = new CreateCreditOperation(bankAccount, balance, ownerId, description);

            Credit credit = new Credit(bankAccount, balance, ownerId);
            boolean ifSucceeded = credits.add(credit);

            if (ifSucceeded) {
                Ack ack = createCreditOperation.execute();
                bankHistory.add(ack);

                return true;
            }
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
    private Credit getCreditById(int id) {
        return credits.stream().filter(account -> account.getId() == id).findFirst().get();
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
    public boolean transfer(int accountFromId, int accountToId, double value) {
        // check if accountFrom exists in bank
        if (ifAccountExists(accountFromId) && ifAccountExists(accountToId)) {
            BankAccount bankAccountFrom = getBankAccountById(accountFromId);
            BankAccount bankAccountTo = getBankAccountById(accountToId);

            String description = "money successfully transferred from account: " + accountFromId + " to: " + accountToId + ", with amount of " + value;
            TransferFromToOperation transferFromToOperation = new TransferFromToOperation(getBankAccountById(accountFromId), getBankAccountById(accountToId), value, description);

            Ack ack = transferFromToOperation.execute();
            bankHistory.add(ack);

            return true;
        }

        return false;
    }

    /**
     * Transfer money from account in another bank
     *
     * @param packageToAnotherBank package we want to send
     * @return
     */
    @Override
    public boolean transferFromAnotherBank(PackageToAnotherBank packageToAnotherBank) {
        if (ifAccountExists(packageToAnotherBank.getToAccount())) {
            BankAccount bankAccount = getBankAccountById(packageToAnotherBank.getToAccount());
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
            PackageToAnotherBank packageToAnotherBankResponse = new PackageToAnotherBank(bankId, packageToAnotherBank.getToAccount(), packageToAnotherBank.getFromBank(), packageToAnotherBank.getFromAccount(), packageToAnotherBank.getValue(), TypeOfPackage.BOUNCED);
            addPackageTolist(packageToAnotherBankResponse);

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
    public boolean payment(int accountId, double value) {
        if (ifAccountExists(accountId)) {
            BankAccount bankAccount = getBankAccountById(accountId);

            String description = "money successfully transferred to account: " + accountId + ", with amount of " + value;
            PaymentOperation paymentOperation = new PaymentOperation(bankAccount, value, description);
            Ack ack = paymentOperation.execute();
            bankAccount.addToHistory(ack);
            bankHistory.add(ack);

            return true;
        }

        return false;
    }

    /**
     * Withdraw money from account
     *
     * @param accountId unique id of account
     * @param value     value of transferring money
     * @return true if withdraw succeeded
     */
    @Override
    public boolean withdraw(int accountId, double value) {
        if (ifAccountExists(accountId)) {
            BankAccount bankAccount = getBankAccountById(accountId);

            String description = "money successfully withdrawn from account: " + accountId + ", with amount of " + value;
            WithdrawOperation withdrawOperation = new WithdrawOperation(bankAccount, value, description);

            Ack ack = withdrawOperation.execute();
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
    private boolean deleteCreditById(int id) {
        // check if client exists
        if (ifCreditExists(id)) {
            Credit credit = getCreditById(id);
            Client client = getClientById(credit.getOwnerId());
            boolean ifSucceeded = credits.removeIf(cr -> cr.getId() == id);

            if (ifSucceeded) {
                // creating ack
                Ack ack = new BankAck(credit.getId(), null, id, TypeOperation.DELETE_CREDIT, LocalDate.now(), "Credit of id: " + id + " of client " + client + " deleted");
                bankHistory.add(ack);

                return true;
            }
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
    public boolean payCreditRate(int creditId, double value) {
        if (ifCreditExists(creditId)) {
            Credit credit = getCreditById(creditId);
            String description = "rate transferred to credit's account with id " + creditId + ", value = " + value;
            PayPercentageOperation payPercentageOperation = new PayPercentageOperation(credit, description);
            boolean ifSucceeded = PayPercentageOperation.payPercentage(credit, description);

            if (ifSucceeded) {
                // credit still must be payed by client
                Ack ack = payPercentageOperation.execute();
                bankHistory.add(ack);
                return true;
            } else {
                // credit is payed off
                Client client = getClientById(credit.getOwnerId());
                String creditPayedOffDescription = "credit of client " + client + " is payed off and can be removed";
                PayOfCreditOperation payOfCreditOperation = new PayOfCreditOperation(credit, creditPayedOffDescription);

                Ack ack = payOfCreditOperation.execute();
                bankHistory.add(ack);
                deleteCreditById(creditId);
                return true;
            }
        }

        return false;
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
    private Deposit getDepositById(int id) {
        return deposits.stream().filter(deposit -> deposit.getId() == id).findFirst().get();
    }

    /**
     * Removing specified by id deposit from bank
     *
     * @param id unique deposit's id
     * @return true if operation succeeded
     */
    private boolean deleteDepositById(int id) {
        // check if client exists
        if (ifDepositExists(id)) {
            Deposit deposit = getDepositById(id);
            Client client = getClientById(deposit.getOwnerId());
            boolean ifSucceeded = deposits.removeIf(dp -> dp.getId() == id);
            if (ifSucceeded) {
                // creating ack
                Ack ack = new BankAck(deposit.getId(), null, id, TypeOperation.DELETE_DEPOSIT, LocalDate.now(), "Deposit of id: " + id + " of client " + client + " deleted");
                bankHistory.add(ack);

                return true;
            }
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
    public boolean withdrawFromDeposit(int depositId) {
        // checking if deposit exists in bank
        if (ifDepositExists(depositId)) {
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

        return false;
    }

    /**
     * Changing percentage of account
     *
     * @param accountId          unique account id
     * @param interestsMechanism new mechanism
     * @return true if operation succeeded
     */
    @Override
    public boolean changeAccountPercentage(int accountId, InterestsMechanism interestsMechanism) {
        if (ifAccountExists(accountId)) {
            BankAccount bankAccount = getBankAccountById(accountId);
            InterestsMechanism newInterestMechanism = interestsMechanism;
            String description = "Amount of account (" + accountId + ") percentage changed from " + bankAccount.getInterestsMechanism() + " to " + newInterestMechanism;
            ChangePercentageOperation changePercentageOperation = new ChangePercentageOperation(bankAccount, interestsMechanism, description);

            Ack ack = changePercentageOperation.execute();
            bankHistory.add(ack);
            return true;
        }

        return false;
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

    //TODO - ack if Operation methods fail?
    //TODO - payPercentage mechanism
    //TODO - correct struktura.png file
    //TODO - own exceptions
}