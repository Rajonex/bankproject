package operationdeposit;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.Deposit;
import services.Product;

import java.time.LocalDate;

public class BreakUpDepositOperation implements Command {



    Deposit deposit;
    String description;

    public BreakUpDepositOperation(Deposit deposit, String description) {
        this.deposit = deposit;
        this.description = description;
    }


    @Override
    public Ack execute()
    {
        Product bankAccount = deposit.getBankAccount();
        double value = deposit.getBalance();

        if (deposit.decreaseBalance(value)) {
            bankAccount.increaseBalance(value);

            Ack ack = new Ack(deposit.getId(), bankAccount.getId(), TypeOperation.TRANSFER, LocalDate.now(), description);
            deposit.addToHistory(ack);
            bankAccount.addToHistory(ack);
            return ack;
        }

        return null;

    }
}