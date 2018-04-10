package operations;

import messages.Ack;

public interface Command {

    public Ack execute();

}
