package operationbank;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.DebetAccountDecorator;

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
        DebetAccountDecorator debetAccountDecorator = new DebetAccountDecorator(ownerId, limit);
        Ack ack = new Ack(debetAccountDecorator, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
        return ack;
    }
}
