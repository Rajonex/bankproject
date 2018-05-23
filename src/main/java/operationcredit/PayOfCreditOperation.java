package operationcredit;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.Credit;
import services.Product;

import java.time.LocalDate;

public class PayOfCreditOperation implements Command {

    Credit credit;
    String description;

    public PayOfCreditOperation(Credit credit, String description) {
        this.credit = credit;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        double remainingValue = credit.getBalance();
        Product bankAccount = credit.getBankAccount();

        if (bankAccount.decreaseBalance(remainingValue)) {
            credit.increaseBalance(remainingValue);

            Ack ack = new Ack(credit.getId(), bankAccount.getId(), TypeOperation.TRANSFER, LocalDate.now(), description);
            credit.addToHistory(ack);
            bankAccount.addToHistory(ack);

            return ack;
        }
        return null;
    }
}