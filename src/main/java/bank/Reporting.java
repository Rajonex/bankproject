package bank;


import history.History;
import messages.Ack;
import messages.TypeOperation;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Reporting {
    private History history;

    public Reporting(History history) {
        this.history = history;
    }

    /**
     * Getting history connected to clients management
     *
     * @return list of reports with client management
     */
    public List<Ack> createClientsReports() {
        List<Ack> clientsHistoryList = history.returnList().stream().filter(ack -> ack.getTypeOperation() == TypeOperation.ADD_NEW_CLIENT || ack.getTypeOperation() == TypeOperation.DELETE_CLIENT).collect(Collectors.toList());
        return clientsHistoryList;
    }

    /**
     * Getting history connected to trnsfers
     *
     * @return list of reports with trnsfers
     */
    public List<Ack> createTransferReports() {
        List<Ack> transferHistoryList = history.returnList().stream().filter(ack -> ack.getTypeOperation() == TypeOperation.TRANSFER).collect(Collectors.toList());
        return transferHistoryList;
    }

    /**
     * Getting history connected to percentage of account
     *
     * @return list of reports with percentage
     */
    public List<Ack> createPercentageReports() {
        List<Ack> percentageHistoryList = history.returnList().stream().filter(ack -> ack.getTypeOperation() == TypeOperation.PAY_PERCENTAGE || ack.getTypeOperation() == TypeOperation.CHANGE_PERCENTAGE).collect(Collectors.toList());
        return percentageHistoryList;
    }

    /**
     * Getting history connected to payments
     *
     * @return list of reports with payments
     */
    public List<Ack> createPaymentReports() {
        List<Ack> paymentHistoryList = history.returnList().stream().filter(ack -> ack.getTypeOperation() == TypeOperation.PAYMENT).collect(Collectors.toList());
        return paymentHistoryList;
    }

    /**
     * Getting history connected to accounts
     *
     * @return list of reports with accounts
     */
    public List<Ack> createAccountsReports() {
        List<Ack> accountHistoryList = history.returnList().stream().filter(ack -> ack.getTypeOperation() == TypeOperation.CREATE_ACCOUNT || ack.getTypeOperation() == TypeOperation.DELETE_ACCOUNT).collect(Collectors.toList());
        return accountHistoryList;
    }

    /**
     * Getting history connected to credits
     *
     * @return list of reports with credits
     */
    public List<Ack> createCreditReports() {
        List<Ack> craditHistoryList = history.returnList().stream().filter(ack -> ack.getTypeOperation() == TypeOperation.DELETE_CREDIT).collect(Collectors.toList());
        return craditHistoryList;
    }

    /**
     * Getting history after date
     *
     * @param localDate date after which we want start history reporting
     * @return list of reports after date
     */
    public List<Ack> createAfterDateReports(LocalDate localDate) {
        List<Ack> afterDateHistoryList = history.returnList().stream().filter(ack -> ack.getLocalDate().isAfter(localDate)).collect(Collectors.toList());
        return afterDateHistoryList;
    }

    /**
     * Getting history before date
     *
     * @param localDate date before which we want start history reporting
     * @return list of reports before date
     */
    public List<Ack> createBeforeDateReports(LocalDate localDate) {
        List<Ack> beforeDateHistoryList = history.returnList().stream().filter(ack -> ack.getLocalDate().isBefore(localDate)).collect(Collectors.toList());
        return beforeDateHistoryList;
    }
}