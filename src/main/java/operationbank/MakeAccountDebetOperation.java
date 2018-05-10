package operationbank;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;
import services.DebetAccountDecorator;
import services.Product;

import java.time.LocalDate;

public class MakeAccountDebetOperation implements Command {

    double limit;
    double debet;
    Product bankAccount;
    String description;

    public MakeAccountDebetOperation(Product bankAccount, double limit, double debet, String description) {
        this.bankAccount = bankAccount;
        this.limit = limit;
        this.debet = debet;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        bankAccount = new DebetAccountDecorator(debet, limit, bankAccount);
        Ack ack = new Ack(bankAccount.getId(), null, TypeOperation.MAKE_DEBET, LocalDate.now(), description);
        bankAccount.addToHistory(ack);
        return ack;
    }
}
