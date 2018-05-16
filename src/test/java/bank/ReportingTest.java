package bank;

import history.History;
import messages.BankAck;
import messages.TypeOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.core.Is.is;

public class ReportingTest {
    Reporting reporting;
    History history;

    @Before
    public void setUp() throws Exception {
        history = new History();
        reporting = new Reporting(history);

        history.add(new BankAck(null, null, 0, TypeOperation.ADD_NEW_CLIENT, LocalDate.now(), "New 0 client"));
        history.add(new BankAck(null, null, 1, TypeOperation.ADD_NEW_CLIENT, LocalDate.now(), "New 1 client"));
        history.add(new BankAck(null, null, 2, TypeOperation.ADD_NEW_CLIENT, LocalDate.now(), "New 2 client"));
        history.add(new BankAck(null, null, 3, TypeOperation.ADD_NEW_CLIENT, LocalDate.now(), "New 3 client"));

        history.add(new BankAck(null, null, 0, TypeOperation.DELETE_CLIENT, LocalDate.now(), "0 client deleted"));
        history.add(new BankAck(null, null, 1, TypeOperation.DELETE_CLIENT, LocalDate.now(), "1 client deleted"));
        history.add(new BankAck(null, null, 2, TypeOperation.DELETE_CLIENT, LocalDate.now(), "2 client deleted"));

        history.add(new BankAck(0, 1, 3, TypeOperation.TRANSFER, LocalDate.of(2006, 12, 12), "Transfer"));
        history.add(new BankAck(0, null, 3, TypeOperation.CHANGE_PERCENTAGE, LocalDate.of(1999, 10, 20), "Change percentage"));
        history.add(new BankAck(0, null, 3, TypeOperation.PAYMENT, LocalDate.of(2018, 01, 01), "Payment"));
        history.add(new BankAck(0, null, 3, TypeOperation.PAYMENT, LocalDate.of(2018, 01, 02), "Payment2"));
        history.add(new BankAck(0, null, 3, TypeOperation.CREATE_ACCOUNT, LocalDate.of(2018, 01, 03), "New Account"));
        history.add(new BankAck(0, null, 3, TypeOperation.PAY_PERCENTAGE, LocalDate.of(1999, 01, 01), "Pay Percentage"));
        history.add(new BankAck(0, null, 3, TypeOperation.DELETE_CREDIT, LocalDate.of(1995, 01, 05), "Deleting credit"));
        history.add(new BankAck(0, null, 3, TypeOperation.CREATE_ACCOUNT, LocalDate.of(2003, 02, 02), "New Account"));
    }

    @Test
    public void createClientsReportsTest() {
        int number = reporting.createClientsReports().size();

        Assert.assertThat(number, is(7));
    }

    @Test
    public void createTransferReportsTest() {
        int number = reporting.createTransferReports().size();

        Assert.assertThat(number, is(1));
    }

    @Test
    public void createPercentageReportsTest() {
        int number = reporting.createPercentageReports().size();

        Assert.assertThat(number, is(2));
    }

    @Test
    public void createPaymentReportsTest() {
        int number = reporting.createPaymentReports().size();

        Assert.assertThat(number, is(2));
    }

    @Test
    public void createAccountsReportsTest() {
        int number = reporting.createAccountsReports().size();

        Assert.assertThat(number, is(2));
    }

    @Test
    public void createCreditReportsTest() {
        int number = reporting.createCreditReports().size();

        Assert.assertThat(number, is(1));
    }

    @Test
    public void createAfterDateReportsTest() {
        int number = reporting.createAfterDateReports(LocalDate.of(2000, 01, 01)).size();

        Assert.assertThat(number, is(12));
    }

    @Test
    public void createBeforeDateReportsTest() {

        int number = reporting.createBeforeDateReports(LocalDate.of(2000, 01, 01)).size();

        Assert.assertThat(number, is(3));
    }
}