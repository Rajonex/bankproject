package operationbank;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;

import java.time.LocalDate;

public class TransferInterbankBouncedOperation implements Command {
    private int bankAccountFrom;
    private BankAccount bankAccountTo;
    private double value;
    private String description;

    /**
     * TODO Konto musi istniec!!!!!!!
     *
     * @param bankAccountFrom
     * @param getBankAccountTo
     * @param value
     * @param description
     */
    public TransferInterbankBouncedOperation(int bankAccountFrom, BankAccount getBankAccountTo, double value, String description) {
        this.bankAccountFrom = bankAccountFrom;
        this.bankAccountTo = getBankAccountTo;
        this.value = value;
        this.description = description;
    }

    @Override
    public Ack execute() {
        bankAccountTo.increaseBalance(value);
        Ack ack = new Ack(bankAccountFrom, bankAccountTo.getId(), TypeOperation.TRANSFER_BOUNCED, LocalDate.now(), description);
        bankAccountTo.addToHistory(ack);
        return ack;

    }
}