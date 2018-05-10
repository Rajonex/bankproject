package operationbank;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;

import java.time.LocalDate;

public class WithdrawOperation implements Command {

    BankAccount bankAccount;
    double value;
    String description;

    public WithdrawOperation(BankAccount bankAccount, double value, String description) {
        this.bankAccount = bankAccount;
        this.value = value;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        if (bankAccount.decreaseBalance(value)) {
            Ack ack = new Ack(null, bankAccount.getId(), TypeOperation.WITHDRAWN, LocalDate.now(), description);
            return ack;
        }
        return null;
    }
}
