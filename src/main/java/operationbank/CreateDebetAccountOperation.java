package operationbank;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.DebetAccount;

import java.time.LocalDate;

public class CreateDebetAccountOperation implements Command {

    int ownerId;
    double limit;
    String description;

    public CreateDebetAccountOperation(int ownerId, double limit, String description) {
        this.ownerId = ownerId;
        this.limit = limit;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        DebetAccount debetAccount = new DebetAccount(ownerId, limit);
        Ack ack = new Ack(debetAccount, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
        return ack;
    }
}
