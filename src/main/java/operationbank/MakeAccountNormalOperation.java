package operationbank;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;
import services.NormalAccount;

import java.time.LocalDate;

public class MakeAccountNormalOperation implements Command {

    BankAccount bankAccount;
    String description;

    public MakeAccountNormalOperation(BankAccount bankAccount, String description) {
        this.bankAccount = bankAccount;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
        if (bankAccount.getBalance() > 0) {
            bankAccount = new NormalAccount(bankAccount);
            Ack ack = new Ack(bankAccount, null, TypeOperation.MAKE_NORMAL, LocalDate.now(), description);
            bankAccount.addToHistory(ack);
            return ack;
        }
        return null;
    }
}
