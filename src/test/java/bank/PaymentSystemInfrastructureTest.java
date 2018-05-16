package bank;

import clients.Client;
import exceptions.NoSuchAccountException;
import exceptions.NoSuchClientException;
import messages.PackageToAnotherBank;
import messages.TypeOfPackage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.BankAccount;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class PaymentSystemInfrastructureTest {
    private PaymentSystemInfrastructure paymentSystemInfrastructure;

    @Before
    public void initial() {
        paymentSystemInfrastructure = new PaymentSystemInfrastructure();
    }

    @Test
    public void createNewBankTest()throws Exception {
        Bank bank = paymentSystemInfrastructure.createNewBank();
        Assert.assertNotNull(bank);
    }

    @Test
    public void sendPackageTest() throws Exception, NoSuchClientException, NoSuchAccountException {
        // TODO - wywalić do pola i inicjować w before
        BankImpl bankImpl = paymentSystemInfrastructure.createNewBank();

        // Adding client and its account to bank
        Client client = new Client("Adam", "Nowak", "25555564433");
        bankImpl.addNewClient(client);
        int clientId = bankImpl.getClients().get(0).getId();
        bankImpl.addNewNormalAccount(clientId);
        int accountClientId = bankImpl.getBankAccounts().get(0).getId();

        List<PackageToAnotherBank> listOfPackages = new ArrayList<>();
        PackageToAnotherBank packageToAnotherBank = new PackageToAnotherBank(0, 0,  bankImpl.getBankId(), accountClientId, 2_000, TypeOfPackage.NORMAL);
        listOfPackages.add(packageToAnotherBank);

        BankAccount bankAccount = bankImpl.getBankAccountById(accountClientId);

        paymentSystemInfrastructure.sendPackages(listOfPackages);
        Assert.assertThat(bankAccount.getBalance(), is((double)2_000));
    }

    @Test
    public void sendPackageNotExistingBankTest() throws Exception {
        List<PackageToAnotherBank> listOfPackages = new ArrayList<>();
        PackageToAnotherBank packageToAnotherBank = new PackageToAnotherBank(0, 0,  0, 0, 2_000, TypeOfPackage.NORMAL);
        listOfPackages.add(packageToAnotherBank);
        boolean ifSucceeded = paymentSystemInfrastructure.sendPackages(listOfPackages);

        Assert.assertFalse(ifSucceeded);
    }

    @Test
    public void sendPackagesToNotExistingBankTest() throws Exception {
        List<PackageToAnotherBank> listOfPackages = new ArrayList<>();
        PackageToAnotherBank packageToAnotherBank = new PackageToAnotherBank(0, 0, 1, 0, 10_000, TypeOfPackage.NORMAL);
        PackageToAnotherBank packageToAnotherBank2 = new PackageToAnotherBank(0, 0, 1, 0, 5_000, TypeOfPackage.NORMAL);
        listOfPackages.add(packageToAnotherBank);
        listOfPackages.add(packageToAnotherBank2);

        boolean ifSucceeded = paymentSystemInfrastructure.sendPackages(listOfPackages);
        Assert.assertFalse(ifSucceeded);
    }
}
