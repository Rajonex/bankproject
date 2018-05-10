package operationdeposit;

import interests.InterestsMechanism;
import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.Deposit;

import java.time.LocalDate;

public class ChangePercentageOperation implements Command {

    Deposit deposit;
    InterestsMechanism mechanism;
    String description;

    public ChangePercentageOperation(Deposit deposit, InterestsMechanism mechanism, String description) {
        this.deposit = deposit;
        this.mechanism = mechanism;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        InterestsMechanism oldMechanism = deposit.getInterestsMechanism();
        deposit.setInterestsMechanism(mechanism);
        Ack ack = new Ack(deposit.getId(), null, TypeOperation.CHANGE_PERCENTAGE, LocalDate.now(),
                "Change percentage from " + oldMechanism + " to "+ mechanism + ". " + description);
        deposit.addToHistory(ack);
        return ack;

    }
}
