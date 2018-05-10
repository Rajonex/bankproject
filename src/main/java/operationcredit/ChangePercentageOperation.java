package operationcredit;

import interests.InterestsMechanism;
import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.Credit;

import java.time.LocalDate;

public class ChangePercentageOperation implements Command {

    Credit credit;
    InterestsMechanism mechanism;
    String description;

    public ChangePercentageOperation(Credit credit, InterestsMechanism mechanism, String description) {
        this.credit = credit;
        this.mechanism = mechanism;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        InterestsMechanism oldMechanism = credit.getInterestsMechanism();
        credit.setInterestsMechanism(mechanism);

        Ack ack = new Ack(credit.getId(), null, TypeOperation.CHANGE_PERCENTAGE, LocalDate.now(),
                "Change percentage from " + oldMechanism + " to " + mechanism + ". " + description);
        credit.addToHistory(ack);

        return ack;
    }
}
