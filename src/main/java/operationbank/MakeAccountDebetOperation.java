package operationbank;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;
import services.DebetAccountDecorator;

import java.time.LocalDate;

public class MakeAccountDebetOperation implements Command {

    BankAccount bankAccount;
    double limit;
    String description;

    public MakeAccountDebetOperation(BankAccount bankAccount, double limit, String description) {
        this.bankAccount = bankAccount;
        this.limit = limit;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        bankAccount = new DebetAccountDecorator(bankAccount, limit);
        Ack ack = new Ack(bankAccount, null, TypeOperation.MAKE_DEBET, LocalDate.now(), description);
        bankAccount.addToHistory(ack);
        return ack;
    }
}
