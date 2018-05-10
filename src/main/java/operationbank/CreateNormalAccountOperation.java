package operationbank;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.NormalAccount;

import java.time.LocalDate;

public class CreateNormalAccountOperation implements Command {

    int ownerId;
    String description;

    public CreateNormalAccountOperation(int ownerId, String description) {
        this.ownerId = ownerId;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        NormalAccount normalAccount = new NormalAccount(ownerId);
        Ack ack = new Ack(normalAccount.getId(), null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
        normalAccount.addToHistory(ack);
        return ack;
    }
}
