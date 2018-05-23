package operationbank;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.Product;

import java.time.LocalDate;

public class TransferInterbankOperation implements Command {
    private int bankAccountFrom;
    private Product bankAccountTo;
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
    public TransferInterbankOperation(int bankAccountFrom, Product getBankAccountTo, double value, String description) {
        this.bankAccountFrom = bankAccountFrom;
        this.bankAccountTo = getBankAccountTo;
        this.value = value;
        this.description = description;
    }

    @Override
    public Ack execute() {
        bankAccountTo.increaseBalance(value);
        Ack ack = new Ack(bankAccountFrom, bankAccountTo.getId(), TypeOperation.TRANSFER, LocalDate.now(), description);
        bankAccountTo.addToHistory(ack);
        return ack;

    }
}