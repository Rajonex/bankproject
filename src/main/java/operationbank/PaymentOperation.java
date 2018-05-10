package operationbank;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;

import java.time.LocalDate;

public class PaymentOperation implements Command {

    BankAccount bankAccount;
    double value;
    String description;

    public PaymentOperation(BankAccount bankAccount, double value, String description) {
        this.bankAccount = bankAccount;
        this.value = value;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        bankAccount.increaseBalance(value);
        Ack ack = new Ack(bankAccount.getId(), null, TypeOperation.PAYMENT, LocalDate.now(), description);
        return ack;
    }

}
