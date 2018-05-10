package operationbank;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;
import services.DebetAccountDecorator;

import java.time.LocalDate;

//TODO do usuniecia
public class CreateDebetAccountOperation implements Command {

    double limit;
    double debet;
    BankAccount bankAccount;
    String description;


    public CreateDebetAccountOperation(BankAccount bankAccount, double debet, double limit, String description) {
        this.limit = limit;
        this.description = description;
        this.bankAccount = bankAccount;
        this.debet = debet;
    }

    @Override
    public Ack execute()
    {
        DebetAccountDecorator debetAccountDecorator = new DebetAccountDecorator(debet, limit, bankAccount);
        Ack ack = new Ack(debetAccountDecorator.getId(), null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
        return ack;
    }
}
