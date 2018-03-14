package bank;

import clients.Client;
import history.History;
import messages.Ack;
import messages.BankAck;
import messages.TypeOperation;
import services.Credit;
import services.DebetAccount;
import services.Deposit;
import services.NormalAccount;

import java.time.LocalDate;
import java.util.List;

public class Bank
{
    private List<Client> clients;

    private List<Credit> credits;
    private List<Deposit> deposits;
    private List<NormalAccount> normalAccounts;
    private List<DebetAccount> debetAccounts;

    private History bankHistory;

    /**
     * Adding new Client to the bank
     * @param client cannot be null and must be unique
     * @return true if operation succeeded
     */
    public boolean addNewClient(Client client)
    {
        // test if list contains client and client is not a null
        if(client != null && !(clients.contains(client)))
        {
            // adding to list and return true if operration succeeded
            boolean ifSucceeded = clients.add(client);
            if(ifSucceeded == true)
            {
                // creating ack
                Ack ack = new BankAck(null, null, client, TypeOperation.ADD_NEW_CLIENT, LocalDate.now(), "New client created");
                bankHistory.add(ack);

                return true;
            }
        }

        return false;
    }

    /**
     * Checking if clients list contains client with specified id
     * @param id unique value of every client
     * @return true if succeeded
     */
    private boolean ifClientsContainsClientById(long id)
    {
        return clients.stream().filter(client -> client.getId() == id).findFirst().isPresent();
    }

    /**
     * Removing Client with specified id from bank
     * @param id client id
     * @return true if succeeded
     */
    public boolean deleteClientById(long id)
    {
        if(ifClientsContainsClientById(id))
        {
            boolean ifSucceeded = clients.removeIf(client -> client.getId() == id);
            if(ifSucceeded == true)
            {
                // creating ack
                Ack ack = new Ack(null, null, TypeOperation.DELETE_CLIENT, LocalDate.now(), "Client deleted");
                bankHistory.add(ack);

                return true;
            }
        }

        return false;
    }
}

/*
TODO:
-creat new class that extends Ack or change Ack (create and delete clients).
-descriptions of ACKs.
-write other methods.
 */