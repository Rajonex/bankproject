package bank;


import clients.Client;
import history.History;
import interests.InterestsMechanism;
import messages.*;
import operationbank.TransferInterbankBouncedOperation;
import operationbank.TransferInterbankOperation;
import operations.BankAccountOperation;
import operations.CreditOperation;
import operations.DepositOperation;
import services.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private static final int MAX_PACKAGE_AMOUNT = 5;
    private final int bankId = IdGenerator.generateBankId();
    private PaymentSystemInfrastructure paymentSystemInfrastructure;
    private List<Client> clients;
    private List<Credit> credits;
    private List<Deposit> deposits;
    private List<BankAccount> bankAccounts;
    private List<PackageToAnotherBank> packagesToSend;
    private History bankHistory;

    public Bank() {
        clients = new ArrayList<>();
        credits = new ArrayList<>();
        deposits = new ArrayList<>();
        bankAccounts = new ArrayList<>();
        packagesToSend = new ArrayList<>();

        bankHistory = new History();
    }

    /**
     * Adding new Client to the bank
     *
     * @param client cannot be null and must be unique
     * @return true if operation succeeded
     */
    public boolean addNewClient(Client client) {
        // test if list contains client and client is not a null
        if (client != null && !(clients.contains(client))) {
            // adding to list and return true if operation succeeded
            boolean ifSucceeded = clients.add(client);
            if (ifSucceeded) {
                // creating ack
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
    public boolean deleteClientById(int id) {
        // check if client exists
        if (ifClientExists(id)) {
            Client client = getClientById(id);
            boolean ifSucceeded = clients.removeIf(cl -> cl.getId() == id);
            if (ifSucceeded) {
                // creating ack
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
    public boolean addNewNormalAccount(int ownerId, double percentage) {
        // check if ownerId is correct and if client with this id exists
        if (ownerId >= 0 && ifClientExists(ownerId)) {
            String description = "Client " + getClientById(ownerId) + " added new account with " + percentage * 100 + " percentage";

            NormalAccount normalAccount = BankAccountOperation.createNormalAccount(ownerId, description);
            boolean ifSucceeded = bankAccounts.add(normalAccount);

            if (ifSucceeded) {
                Ack ack = new Ack(normalAccount, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
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
     * @return true if operation succeeded
     */
    public boolean makeAccountDebet(int accountId, double limit) {
        if (ifAccountExists(accountId)) {
            BankAccount bankAccount = getBankAccountById(accountId);
            Client client = getClientById(bankAccount.getOwnerId());
            String description = "Client " + client + " account changed to debet account with " + limit + " limit";
            boolean ifSucceeded = BankAccountOperation.makeAccountDebet(bankAccount, limit, description);

            if (ifSucceeded) {
                Ack ack = new Ack(bankAccount, null, TypeOperation.MAKE_DEBET, LocalDate.now(), description);
                bankHistory.add(ack);
                return true;
            }
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
    public boolean addNewDebetAccount(int ownerId, double limit, double percentage) {
        // check if ownerId is correct and if client with this id exists
        if (ownerId >= 0 && ifClientExists(ownerId)) {
            String description = "Client " + getClientById(ownerId) + " added new debet account with " + limit + " and " + percentage * 100 + " percentage";

            DebetAccount debetAccount = BankAccountOperation.createDebetAccount(ownerId, limit, description);
            boolean ifSucceeded = bankAccounts.add(debetAccount);

            if (ifSucceeded) {
                Ack ack = new Ack(debetAccount, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
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
    public boolean makeAccountNormal(int accountId) {
        if (ifAccountExists(accountId)) {
            BankAccount bankAccount = getBankAccountById(accountId);
            Client client = getClientById(bankAccount.getOwnerId());
            String description = "Client " + client + " account changed to normal account";
            boolean ifSucceeded = BankAccountOperation.makeAccountNormal(bankAccount, description);

            if (ifSucceeded) {
                Ack ack = new Ack(bankAccount, null, TypeOperation.MAKE_NORMAL, LocalDate.now(), description);
                bankAccount.addToHistory(ack);
                return true;
            }
            //else
            // TODO - create ack that operation failed?
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
        return bankAccounts.stream().filter(account -> account.getId() == accountId).findFirst().get();
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
    public boolean addNewDeposit(int accountId, double value, int ownerId, double percentage) {
        // check if client and its account exists
        if (ifClientAccountExists(ownerId)) {
            Client client = getClientById(ownerId);
            String description = "New " + client.getFirstName() + " " + client.getLastName() + " deposit with " + value + " and " + percentage * 100 + " created";
            Deposit deposit = DepositOperation.createDeposit(getBankAccountById(accountId), value, ownerId, description);

            boolean ifSucceeded = deposits.add(deposit);

            if (ifSucceeded) {
                Ack ack = new Ack(deposit, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
                Ack ackBankAccount = new Ack(getBankAccountById(accountId), deposit, TypeOperation.TRANSFER, LocalDate.now(), description);

                bankHistory.add(ack);
                bankHistory.add(ackBankAccount);

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
    public boolean addNewCredit(int accountId, double balance, int ownerId, double percentage) {
        // check if client and its account exists
        if (ifClientAccountExists(ownerId)) {
            Client client = getClientById(ownerId);
            String description = "New " + client.getFirstName() + " " + client.getLastName() + " credit with " + balance + " and " + percentage * 100 + " created";
            Credit credit = CreditOperation.createCredit(getBankAccountById(accountId), balance, ownerId, description);

            boolean ifSucceeded = credits.add(credit);

            if (ifSucceeded) {
                Ack ack = new Ack(credit, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
                Ack ackBankAccount = new Ack(credit, getBankAccountById(accountId), TypeOperation.TRANSFER, LocalDate.now(), description);

                bankHistory.add(ack);
                bankHistory.add(ackBankAccount);

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
    public boolean transfer(int accountFromId, int accountToId, double value) {
        // check if accountFrom exists in bank
        if (ifAccountExists(accountFromId) && ifAccountExists(accountToId)) {
            BankAccount bankAccountFrom = getBankAccountById(accountFromId);
            BankAccount bankAccountTo = getBankAccountById(accountToId);

            String description = "money successfully transferred from account: " + accountFromId + " to: " + accountToId + ", with amount of " + value;
            boolean ifSucceeded = BankAccountOperation.transferFromTo(bankAccountFrom, bankAccountTo, value, description);

            if (ifSucceeded) {
                Ack ack = new Ack(bankAccountFrom, bankAccountTo, TypeOperation.TRANSFER, LocalDate.now(), description);
                bankHistory.add(ack);

                return true;
            }
            //else
            //TODO - ack (przelew się nie powiódł)

        }

        return false;
    }

    // TODO - dodać komentarz
    public boolean transferFromAnotherBank(PackageToAnotherBank packageToAnotherBank) {
        if (ifAccountExists(packageToAnotherBank.getToAccount())) {
//           TODO - Nowa operacja, która doda do ACK transferFromAnotherBank i przeleje hajsy do konta w tym banku
            BankAccount bankAccount = getBankAccountById(packageToAnotherBank.getToAccount());
            if(packageToAnotherBank.getTypeOfPackage() == TypeOfPackage.NORMAL) {
                TransferInterbankOperation transferInterbankOperation = new TransferInterbankOperation(packageToAnotherBank.getFromAccount(), bankAccount, packageToAnotherBank.getValue(), "Interbank transfer");
                transferInterbankOperation.execute();
                // TODO - ACK!!!!
            }else {
                if (packageToAnotherBank.getTypeOfPackage() == TypeOfPackage.BOUNCED) {
                    TransferInterbankBouncedOperation transferInterbankBouncedOperation = new TransferInterbankBouncedOperation(packageToAnotherBank.getFromAccount(), bankAccount, packageToAnotherBank.getValue(), "Interbank transfer");
                    transferInterbankBouncedOperation.execute();
                    // TODO - ACK!!!!
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
    public boolean payment(int accountId, double value) {
        if (ifAccountExists(accountId)) {
            BankAccount bankAccount = getBankAccountById(accountId);

            String description = "money successfully transferred to account: " + accountId + ", with amount of " + value;
            boolean ifSucceeded = BankAccountOperation.payment(bankAccount, value, description);

            if (ifSucceeded) {
                Ack ack = new Ack(bankAccount, null, TypeOperation.PAYMENT, LocalDate.now(), description);
                bankHistory.add(ack);

                return true;
            }
            //TODO - else ack
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
    public boolean withdraw(int accountId, double value) {
        if (ifAccountExists(accountId)) {
            BankAccount bankAccount = getBankAccountById(accountId);

            String description = "money successfully withdrawn from account: " + accountId + ", with amount of " + value;
            boolean ifSucceeded = BankAccountOperation.withdraw(bankAccount, value, description);

            if (ifSucceeded) {
                Ack ack = new Ack(null, bankAccount, TypeOperation.WITHDRAWN, LocalDate.now(), description);
                bankHistory.add(ack);

                return true;
            }
            //TODO - else ack
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
                Ack ack = new BankAck(credit, null, id, TypeOperation.DELETE_CREDIT, LocalDate.now(), "Credit of id: " + id + " of client " + client + " deleted");
                bankHistory.add(ack);

                return true;
            }
        }

        return false;
    }

    public boolean payCreditRate(int creditId, double value) {
        if (ifCreditExists(creditId)) {
            Credit credit = getCreditById(creditId);
            String description = "rate transferred to credit's account with id " + creditId + ", value = " + value;
            boolean ifSucceeded = CreditOperation.transfer(credit, value, description);

            if (ifSucceeded) {
                // credit still must be payed by client
                Ack ack = new Ack(credit, credit.getBankAccount(), TypeOperation.TRANSFER, LocalDate.now(), description);
                bankHistory.add(ack);

                return true;
            } else {
                // credit is payed off
                Client client = getClientById(credit.getOwnerId());
                String creditPayedOff = "credit of client " + client + " is payed off and can be removed";
                boolean ifPayOffSucceeded = CreditOperation.payOfCredit(credit, description);

                if (ifPayOffSucceeded) {
                    Ack ack = new Ack(credit, credit.getBankAccount(), TypeOperation.TRANSFER, LocalDate.now(), description);
                    bankHistory.add(ack);
                    deleteCreditById(creditId);
                    return true;
                }
                //TODO - else now money or something wrong happend
            }
        }

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
                Ack ack = new BankAck(deposit, null, id, TypeOperation.DELETE_DEPOSIT, LocalDate.now(), "Deposit of id: " + id + " of client " + client + " deleted");
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
    public boolean withdrawFromDeposit(int depositId) {
        // checking if deposit exists in bank
        if (ifDepositExists(depositId)) {
            Deposit deposit = getDepositById(depositId);
            Client client = getClientById(deposit.getOwnerId());
            if (deposit.isExpired()) {
                // deposit has expired
                String description = "deposit " + depositId + " of client " + client + " is solved";
                boolean ifSucceeded = DepositOperation.solveDeposit(deposit, description);

                if (ifSucceeded) {
                    Ack ack = new Ack(deposit, deposit.getBankAccount(), TypeOperation.TRANSFER, LocalDate.now(), description);
                    bankHistory.add(ack);

                    return true;
                }
                //TODO - else ACK - wrong
            } else {
                //deposit will be broken up
                String description = "deposit " + depositId + " of client " + client + " is broken up";
                boolean ifSucceeded = DepositOperation.breakUpDeposit(deposit, description);

                if (ifSucceeded) {
                    Ack ack = new Ack(deposit, deposit.getBankAccount(), TypeOperation.TRANSFER, LocalDate.now(), description);
                    bankHistory.add(ack);

                    return true;
                }
                //TODO - else ACK - wrong
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
    public boolean changeAccountPercentage(int accountId, InterestsMechanism interestsMechanism) {
        if (ifAccountExists(accountId)) {
            BankAccount bankAccount = getBankAccountById(accountId);
            InterestsMechanism newInterestMechanism = interestsMechanism;
            String description = "Amount of account (" + accountId + ") percentage changed from " + bankAccount.getInterestsMechanism() + " to " + newInterestMechanism;
            boolean ifSucceeded = BankAccountOperation.changePercentage(bankAccount, newInterestMechanism, description);

            if (ifSucceeded) {
                Ack ack = new Ack(bankAccount, null, TypeOperation.CHANGE_PERCENTAGE, LocalDate.now(), description);
                bankHistory.add(ack);
                return true;
            }
        }

        return false;
    }

    /**
     * Changing percentage of credit
     *
     * @param creditId      unique credit id
     * @param newPercentage new value of percentage
     * @return true if operation succeeded
     */
    public boolean changeCreditPercentage(int creditId, double newPercentage) {
        return false;
    }

    /**
     * Getting list of bank history
     *
     * @return bank history
     */
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

    //TODO - changePercentage (BA, CO, DO)

    // Many
    //TODO - payPercentage (BA, CO)

    //TODO - ack if Operation methods fail?
    //TODO - payPercentage mechanism
    //TODO - correct struktura.png file

    //TODO - bank jako singleton ?
    //TODO - własne wyjątki

}