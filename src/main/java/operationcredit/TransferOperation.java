package operationcredit;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;
import services.Credit;

import java.time.LocalDate;

public class TransferOperation implements Command {

    Credit credit;
    double value;
    String description;

    public TransferOperation(Credit credit, double value, String description) {
        this.credit = credit;
        this.value = value;
        this.description = description;
    }


    @Override
    public Ack execute()
    {
        BankAccount bankAccount = credit.getBankAccount();

        if (credit.increaseBalance(value)) {
            if (bankAccount.decreaseBalance(value)) {

                Ack ack = new Ack(credit.getId(), bankAccount.getId(), TypeOperation.TRANSFER, LocalDate.now(), description);
                credit.addToHistory(ack);
                bankAccount.addToHistory(ack);

                return ack;
            } else {
                credit.decreaseBalance(value);
            }
        }

        return null;
    }
}
