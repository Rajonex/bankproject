package operationbank;

import interests.InterestsMechanism;
import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;
import services.DebetAccount;
import services.NormalAccount;

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

    public static class CreateDebetAccountOperation implements Command{

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

    public static class CreateNormalAccountOperation implements Command {

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
            Ack ack = new Ack(normalAccount, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
            normalAccount.addToHistory(ack);
            return ack;
        }
    }
}
