package bank;

import clients.Client;
import history.History;
import messages.Ack;
import messages.BankAck;
import messages.TypeOperation;
import operations.BankAccountOperation;
import operations.CreditOperation;
import operations.DepositOperation;
import services.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Bank
{
    private List<Client> clients;

    private List<Credit> credits;
    private List<Deposit> deposits;
    private List<BankAccount> bankAccounts;

    private History bankHistory;

    public Bank()
    {
        clients = new ArrayList<>();
        credits = new ArrayList<>();
        deposits = new ArrayList<>();
        bankAccounts = new ArrayList<>();

        bankHistory = new History();
    }

    /**
     * Adding new Client to the bank
     *
     * @param client cannot be null and must be unique
     * @return true if operation succeeded
     */
    public boolean addNewClient(Client client)
    {
        // test if list contains client and client is not a null
        if (client != null && !(clients.contains(client)))
        {
            // adding to list and return true if operation succeeded
            boolean ifSucceeded = clients.add(client);
            if (ifSucceeded)
            {
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
    private boolean ifClientExists(int id)
    {
        return clients.stream().filter(client -> client.getId() == id).findFirst().isPresent();
    }

    /**
     * Getting client from list by specified id
     *
     * @param id unique id of every client
     * @return
     */
    private Client getClientById(int id)
    {
        return clients.stream().filter(cl -> cl.getId() == id).findFirst().get();
    }

    /**
     * Removing Client with specified id from bank
     *
     * @param id client unique id
     * @return true if succeeded
     */
    public boolean deleteClientById(int id)
    {
        // check if client exists
        if (ifClientExists(id))
        {
            Client client = getClientById(id);
            boolean ifSucceeded = clients.removeIf(cl -> cl.getId() == id);
            if (ifSucceeded)
            {
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
    public boolean addNewNormalAccount(int ownerId, double percentage)
    {
        // check if ownerId is correct and if client with this id exists
        if (ownerId >= 0 && ifClientExists(ownerId))
        {
            String description = "Client " + getClientById(ownerId) + " added new account with " + percentage * 100 + " percentage";

            NormalAccount normalAccount = BankAccountOperation.createNormalAccount(ownerId, percentage, bankHistory, description);
            boolean ifSucceeded = bankAccounts.add(normalAccount);

            if (ifSucceeded)
                return true;
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
    public boolean makeAccountDebet(int accountId, double limit)
    {
        if (ifAccountExists(accountId))
        {
            BankAccount bankAccount = getBankAccountById(accountId);
            Client client = getClientById(bankAccount.getOwnerId());
            String description = "Client " + client + " account changed to debet account with " + limit + " limit";
            boolean ifSucceeded = BankAccountOperation.makeAccountDebet(bankAccount, limit, bankHistory, description);

            if (ifSucceeded)
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
    public boolean addNewDebetAccount(int ownerId, double limit, double percentage)
    {
        // check if ownerId is correct and if client with this id exists
        if (ownerId >= 0 && ifClientExists(ownerId))
        {
            String description = "Client " + getClientById(ownerId) + " added new debet account with " + limit + " and " + percentage * 100 + " percentage";

            DebetAccount debetAccount = BankAccountOperation.createDebetAccount(ownerId, limit, percentage, bankHistory, description);
            boolean ifSucceeded = bankAccounts.add(debetAccount);

            if (ifSucceeded)
                return true;
        }

        return false;
    }

    /**
     * Changing account type from debet to normal one.
     *
     * @param accountId account's unique id
     * @return true if operation succeeded
     */
    public boolean makeAccountNormal(int accountId)
    {
        if (ifAccountExists(accountId))
        {
            BankAccount bankAccount = getBankAccountById(accountId);
            Client client = getClientById(bankAccount.getOwnerId());
            String description = "Client " + client + " account changed to normal account";
            boolean ifSucceeded = BankAccountOperation.makeAccountNormal(bankAccount, bankHistory, description);

            if (ifSucceeded)
                return true;
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
    private boolean ifAccountExists(int accountId)
    {
        return bankAccounts.stream().filter(account -> account.getId() == accountId).findFirst().isPresent();
    }

    /**
     * Checking if client's specific account exists
     *
     * @param clientId client's unique id
     * @return
     */
    private boolean ifClientAccountExists(int clientId)
    {
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
    private BankAccount getBankAccountById(int accountId)
    {
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
    public boolean addNewDeposit(int accountId, double value, int ownerId, double percentage)
    {
        // check if client and its account exists
        if (ifClientAccountExists(ownerId))
        {
            Client client = getClientById(ownerId);
            String description = "New " + client.getFirstName() + " " + client.getLastName() + " deposit with " + value + " and " + percentage * 100 + " created";
            Deposit deposit = DepositOperation.createDeposit(getBankAccountById(accountId), value, ownerId, percentage, bankHistory, description);

            boolean ifSucceeded = deposits.add(deposit);

            if (ifSucceeded)
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
    public boolean addNewCredit(int accountId, double balance, int ownerId, double percentage)
    {
        // check if client and its account exists
        if (ifClientAccountExists(ownerId))
        {
            Client client = getClientById(ownerId);
            String description = "New " + client.getFirstName() + " " + client.getLastName() + " credit with " + balance + " and " + percentage * 100 + " created";
            Credit credit = CreditOperation.createCredit(getBankAccountById(accountId), balance, ownerId, percentage, bankHistory, description);

            boolean ifSucceeded = credits.add(credit);

            if (ifSucceeded)
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
    private boolean ifCreditExists(int id)
    {
        return credits.stream().filter(credit -> credit.getId() == id).findFirst().isPresent();
    }

    /**
     * Getting specified credit by id
     *
     * @param id credit's unique id
     * @return credit with specified in param id
     */
    private Credit getCreditById(int id)
    {
        return credits.stream().filter(account -> account.getId() == id).findFirst().get();
    }

    // -------------------------------------------------------------------------------- Transfers

    /**
     * Transfer money from one account to another
     *
     * @param fromId unique id of account where money are taking from
     * @param toId   unique id of account where money are transferring to
     * @param value  value of transferring money between accounts
     * @return true if transfer succeeded
     */
    public boolean transfer(int fromId, int toId, double value)
    {
        // check if accountFrom exists in bank
        if (ifAccountExists(fromId))
        {
            BankAccount bankAccountFrom = getBankAccountById(fromId);

            // check if accountTo exists in bank
            if (ifAccountExists(toId))
            {
                BankAccount bankAccountTo = getBankAccountById(toId);

                String description = "money successfully transferred from account: " + fromId + " to: " + toId + ", with amount of " + value;
                boolean ifSucceeded = BankAccountOperation.transferFromTo(bankAccountFrom, bankAccountTo, value, description, bankHistory);

                if (ifSucceeded)
                    return true;
                //TODO - else ack (przelew się nie powiódł)
            }

            // check if creditTo exists
            else if (ifCreditExists(toId))
            {
                Credit creditTo = getCreditById(toId);

                //TODO - if this Ack needed
                String descriptionForBankAck = "money successfully transferred from account: " + fromId + " to credit: " + toId + ", value = " + value;
                String descriptionForAccountWithdraw = "money successfully withdrawn from account: " + fromId + ", value = " + value;
                String descriptionForCreditPaymentOk = "money successfully transfer to credit: " + toId + ", value = " + value;

                boolean ifWithdrawSucceeded = BankAccountOperation.withdraw(bankAccountFrom, value, descriptionForAccountWithdraw, bankHistory);
                if (ifWithdrawSucceeded)
                {
                    // money withdrawn, lets transfer them into credit
                    boolean ifTransferCreditSucceeded = CreditOperation.transfer(creditTo, value, descriptionForCreditPaymentOk, bankHistory);
                    if (ifWithdrawSucceeded)
                    {
                        // everything alright, return true
                        return true;
                    }
                    else
                    {

                    }
                    //TODO - else cannot transfer money into credit
                }
                //TODO - else ack cannot withdrw money from account

                // payment to credit

                // check if credit is full?
            }
        }

        return false;
    }

    //TODO - przelew (BA<->BA, BA->CO, DO->BA)
    //TODO - wpłata (BA, CO)
    //TODO - wypłata (BA, DO)
    //TODO - changePercentage (BA, CO, DO)

    // Many
    // TODO - payPercentage (BA, CO)

    // TODO - ack if Operation methods fail?
}

/*
Uwagi od Zosi:
TODO - jak masz w CreditOperation metode transfer to pamiętaj, że jeżeli ta metoda Ci zwróci false tzn, że kredytu już jest mniej niż rata, którą próbuje klient wpłacić i wtedy trzeba wywołać metode payOfCredit
TODO - w skrócie mówiąc musisz zrobić tak:
if(CreditOperation.transfer(...))
{
// spłacono ratę
}
else{
// nie spłacono raty
if(CreditOperation.payOfCredit(...))
{
// spłacono kredyt do końca, można go usunąć
}
else{
// brak środków na koncie aby spłacić ratę kredytu albo cholera wie co się wydarzyło
}
}
 */


// TODO - correct struktura.png file