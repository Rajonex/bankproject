package operationcredit;

import interests.InterestsMechanism;
import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.Credit;
import services.Product;

import java.time.LocalDate;

public class CreateCreditOperation implements Command {


    Product bankAccount;
    double balance;
    InterestsMechanism interestsMechanism;
    int ownerId;
    String description;

    public CreateCreditOperation(Product bankAccount, double balance, int ownerId, InterestsMechanism interestsMechanism, String description) {
        this.bankAccount = bankAccount;
        this.balance = balance;
        this.ownerId = ownerId;
        this.description = description;
        this.interestsMechanism = interestsMechanism;
    }

    @Override
    public Ack execute()
    {
        Credit credit = new Credit(bankAccount, balance * (-1), ownerId, interestsMechanism);

        Ack ack = new Ack(credit.getId(), null, TypeOperation.CREATE_ACCOUNT, LocalDate.now(), description);
        Ack ackBankAccount = new Ack(credit.getId(), bankAccount.getId(), TypeOperation.TRANSFER, LocalDate.now(), description);

        credit.addToHistory(ack);
        credit.addToHistory(ackBankAccount);

        bankAccount.increaseBalance(balance);
        return ack;

    }
}