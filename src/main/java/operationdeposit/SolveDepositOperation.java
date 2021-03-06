package operationdeposit;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.Deposit;
import services.Product;

import java.time.LocalDate;

public class SolveDepositOperation implements Command {


    Deposit deposit;
    String description;

    public SolveDepositOperation(Deposit deposit, String description) {
        this.deposit = deposit;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        Product bankAccount = deposit.getBankAccount();
        double value = deposit.getBalance();
        double interest = deposit.getInterests();

        if (deposit.decreaseBalance(value)) {
            double newValue = value + interest;
            bankAccount.increaseBalance(newValue);

            Ack ack = new Ack(deposit.getId(), bankAccount.getId(), TypeOperation.TRANSFER, LocalDate.now(), description);
            deposit.addToHistory(ack);
            bankAccount.addToHistory(ack);

            return ack;
        }

        Ack ack = new Ack(deposit.getId(), bankAccount.getId(), TypeOperation.FAILURE, LocalDate.now(), description);
        deposit.addToHistory(ack);
        bankAccount.addToHistory(ack);

        return ack;

    }
}