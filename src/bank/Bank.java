package bank;

import clients.Client;
import history.History;
import messages.Ack;
import messages.BankAck;
import messages.TypeOperation;
import operations.BankAccountOperation;
import services.Credit;
import services.DebetAccount;
import services.Deposit;
import services.NormalAccount;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Bank
{
    private List<Client> clients;

    private List<Credit> credits;
    private List<Deposit> deposits;
    private List<NormalAccount> normalAccounts;
    private List<DebetAccount> debetAccounts;

    private History bankHistory;

    public Bank()
    {
        clients = new ArrayList<>();
        credits = new ArrayList<>();
        deposits = new ArrayList<>();
        normalAccounts = new ArrayList<>();
        debetAccounts = new ArrayList<>();

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
    private boolean ifClientExist(int id)
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
     * @param id client id
     * @return true if succeeded
     */
    public boolean deleteClientById(int id)
    {
        // check if client exists
        if (ifClientExist(id))
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

    /**
     * Creating new normal Account for client with specified id, percentage and adding it to normalAccounts list.
     *
     * @param ownerId    client's id
     * @param percentage account's percentage
     * @return true if opeartion succeeded
     */
    public boolean addNewNormalAccount(int ownerId, double percentage)
    {
        // check if ownerId is correct and if client with this id exists
        if (ownerId >= 0 && ifClientExist(ownerId))
        {
            String description = "Client " + getClientById(ownerId) + " added new account with " + percentage * 100 + " percentage";

            NormalAccount normalAccount = BankAccountOperation.createNormalAccount(ownerId, percentage, bankHistory, description);
            boolean ifSucceeded = normalAccounts.add(normalAccount);

            if (ifSucceeded)
                return true;
        }

        return false;

    }

    /**
     * Creating new debetAccount for client with specified id, limit, percentage and adding it to debetAccounts list.
     *
     * @param ownerId    client's id
     * @param limit      account's limit
     * @param percentage account's percentage
     * @return true if operation succeeded
     */
    public boolean addNewDebetAccount(int ownerId, double limit, double percentage)
    {
        // check if ownerId is correct and if client with this id exists
        if (ownerId >= 0 && ifClientExist(ownerId))
        {
            String description = "Client " + getClientById(ownerId) + " added new debet account with " + limit + " and " + percentage * 100 + " percentage";

            DebetAccount debetAccount = BankAccountOperation.createDebetAccount(ownerId, limit, percentage, bankHistory, description);
            boolean ifSucceeded = debetAccounts.add(debetAccount);

            if (ifSucceeded)
                return true;
        }

        return false;
    }

    // BankAccountOperation (All ?)
    // TODO - change from normal to debet account
    // TODO - change from debet to normal account

    // CreditOperation
    // TODO - addNewCreditAccount
    // TODO - payOfCredit
    // TODO - transfer

    // DepositOperation
    // TODO - addNewDepositAccount
    // TODO - breakUpDeposit
    // TODO - solveDeposit

    // All
    // TODO - payment (BA)
    // TODO - withdraw (BA)
    // TODO - transferFromTo (BA)
    // TODO - payPercentage (BA, CO)
    // TODO - changePercentage (BA, CO, DO)
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