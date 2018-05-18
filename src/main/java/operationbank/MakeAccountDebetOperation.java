package operationbank;

import bank.Bank;
import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.DebetAccountDecorator;
import services.Product;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class MakeAccountDebetOperation implements Command {

    double limit;
    double debet;
    Product bankAccount;
    String description;
    Bank bank;

    public MakeAccountDebetOperation(Bank bank, Product bankAccount, double limit, String description) {
        this.bankAccount = bankAccount;
        this.limit = limit;
        this.debet = 0;
        this.description = description;
        this.bank = bank;
    }

    @Override
    public Ack execute()
    {
        Ack ack;
        if(bank.wrapAccountFromNormalToDebet(bankAccount.getId(), limit, description))
        {
            ack = new Ack(bankAccount.getId(), null, TypeOperation.MAKE_DEBET, LocalDate.now(), description);
        }
        else
        {
            ack = new Ack(bankAccount.getId(), null, TypeOperation.FAILURE, LocalDate.now(), "Failed during: " + description);
        }
        bankAccount.addToHistory(ack);

        return ack;
    }
}