package operationcredit;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.BankAccount;
import services.Credit;

import java.time.LocalDate;

public class CreateCreditOperation implements Command {


    BankAccount bankAccount;
    double balance;
    int ownerId;
    String description;

    public CreateCreditOperation(BankAccount bankAccount, double balance, int ownerId, String description) {
        this.bankAccount = bankAccount;
        this.balance = balance;
        this.ownerId = ownerId;
        this.description = description;
    }

    @Override
    public Ack execute()
    {
            Credit credit = new Credit(bankAccount, balance * (-1), ownerId);

            Ack ack = new Ack(credit.getId(), null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
            Ack ackBankAccount = new Ack(credit.getId(), bankAccount.getId(), TypeOperation.TRANSFER, LocalDate.now(), description);

            credit.addToHistory(ack);
            credit.addToHistory(ackBankAccount);

            bankAccount.increaseBalance(balance);
            return ack;

    }
}
