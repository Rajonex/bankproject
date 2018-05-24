package operationdeposit;

import interests.InterestsMechanism;
import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.Deposit;
import services.Product;

import java.time.LocalDate;

public class CreateDepositOperation implements Command {


    Product bankAccount;
    double value;
    long duration;
    int ownerId;
    InterestsMechanism interestsMechanism;
    String description;

    public CreateDepositOperation(Product bankAccount, double value, int ownerId, long duration, InterestsMechanism interestsMechanism, String description) {
        this.bankAccount = bankAccount;
        this.value = value;
        this.ownerId = ownerId;
        this.duration = duration;
        this.interestsMechanism = interestsMechanism;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        if (bankAccount.decreaseBalance(value)) {
            Deposit deposit = new Deposit(bankAccount, value, ownerId, duration, interestsMechanism);

            Ack ack = new Ack(deposit.getId(), null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
            Ack ackBankAccount = new Ack(bankAccount.getId(), deposit.getId(), TypeOperation.TRANSFER, LocalDate.now(), description);
            deposit.addToHistory(ack);

            deposit.addToHistory(ackBankAccount);
            bankAccount.addToHistory(ackBankAccount);

            return ack;
        }
        return null;

    }
}