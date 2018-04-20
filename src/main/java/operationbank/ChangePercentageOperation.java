package operationbank;

import interests.InterestsMechanism;
import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;

import java.time.LocalDate;

public class ChangePercentageOperation implements Command {

    BankAccount bankAccount;
    InterestsMechanism mechanism;
    String description;

    public ChangePercentageOperation(BankAccount bankAccount, InterestsMechanism mechanism, String description) {
        this.bankAccount = bankAccount;
        this.mechanism = mechanism;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        InterestsMechanism oldMechanism = bankAccount.getInterestsMechanism();
        bankAccount.setInterestsMechanism(mechanism);

        Ack ack = new Ack(bankAccount, null, TypeOperation.CHANGE_PERCENTAGE, LocalDate.now(), "Change percentage from " + oldMechanism  + " to " + mechanism + ". " + description);
        bankAccount.addToHistory(ack);
        return ack;
    }
}
