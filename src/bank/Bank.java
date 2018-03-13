package bank;

import clients.Client;
import history.History;
import services.*;

public class Bank
{
    private List<Client> clients;

    private List<Credit> credits;
    private List<Deposit> deposits;
    private List<NormalAccount> normalAccounts;
    private List<DebetAccount> debetAccounts;

    private History history;

}
