package operationdeposit;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;
import services.Deposit;

import java.time.LocalDate;

public class CreateDepositOperation implements Command {


    BankAccount bankAccount;
    double value;
    int ownerId;
    String description;

    public CreateDepositOperation(BankAccount bankAccount, double value, int ownerId, String description) {
        this.bankAccount = bankAccount;
        this.value = value;
        this.ownerId = ownerId;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
             if (bankAccount.decreaseBalance(value)) {
                Deposit deposit = new Deposit(bankAccount, value, ownerId);

                Ack ack = new Ack(deposit, null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
                Ack ackBankAccount = new Ack(bankAccount, deposit, TypeOperation.TRANSFER, LocalDate.now(), description);
                deposit.addToHistory(ack);

                deposit.addToHistory(ackBankAccount);
                bankAccount.addToHistory(ackBankAccount);

                return ack;
            }
            return null;

    }
}
