package operationbank;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;
import services.Product;

import java.time.LocalDate;

public class PayPercentageOperation implements Command {


    Product bankAccount;
    String description;

    public PayPercentageOperation(Product bankAccount, String description) {
        this.bankAccount = bankAccount;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
            double balance = bankAccount.getBalance();
            if (balance > 0) {
                bankAccount.increaseBalance(bankAccount.getInterests());
                Ack ack = new Ack(bankAccount.getId(), null, TypeOperation.PAY_PERCENTAGE, LocalDate.now(), description);
                bankAccount.addToHistory(ack);
                return ack;
            }

            return null;
    }
}
