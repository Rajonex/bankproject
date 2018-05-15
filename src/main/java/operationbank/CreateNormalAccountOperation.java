package operationbank;

import messages.Ack;
import operations.Command;

public class CreateNormalAccountOperation implements Command {
    int ownerId;
    String description;

    public CreateNormalAccountOperation(int ownerId, String description) {
        this.description = description;
        this.ownerId = ownerId;
    }

    @Override
    public Ack execute() {
        return null;
    }
}
