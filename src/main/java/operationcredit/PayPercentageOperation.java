package operationcredit;

import messages.Ack;
import messages.TypeOperation;
import operations.Command;
import services.Credit;

import java.time.LocalDate;

public class PayPercentageOperation implements Command {

    public static boolean payPercentage(Credit credit, String description) {
        boolean flag = false;
        //BankAccount bankAccount = credit.getBankAccount();
        double balance = credit.getBalance();

        if (balance < 0) {
            credit.decreaseBalance(credit.getInterests());
            flag = true;

            Ack ack = new Ack(credit.getId(), null, TypeOperation.PAY_PERCENTAGE, LocalDate.now(), description);
            credit.addToHistory(ack);
        }

        return flag;
    }


    Credit credit;
    String description;


    public PayPercentageOperation(Credit credit, String description) {
        this.credit = credit;
        this.description = description;
    }


    @Override
    public Ack execute()
    {
        double balance = credit.getBalance();

        if (balance < 0) {
            credit.decreaseBalance(credit.getInterests());
            Ack ack = new Ack(credit.getId(), null, TypeOperation.PAY_PERCENTAGE, LocalDate.now(), description);
            credit.addToHistory(ack);
            return ack;
        }

        return null;
    }
}
