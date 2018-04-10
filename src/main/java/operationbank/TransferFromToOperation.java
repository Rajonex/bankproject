package operationbank;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;

import java.time.LocalDate;

public class TransferFromToOperation implements Command {

    BankAccount bankAccountFrom;
    BankAccount bankAccountTo;
    double value;
    String description;

    public TransferFromToOperation(BankAccount bankAccountFrom, BankAccount getBankAccountTo, double value, String description) {
        this.bankAccountFrom = bankAccountFrom;
        this.bankAccountTo = getBankAccountTo;
        this.value = value;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        if (bankAccountFrom.decreaseBalance(value)) {
            bankAccountTo.increaseBalance(value);
            Ack ack = new Ack(bankAccountFrom, bankAccountTo, TypeOperation.TRANSFER, LocalDate.now(), description);
            bankAccountFrom.addToHistory(ack);
            bankAccountTo.addToHistory(ack);
            return ack;
        }
        return null;
    }















}
