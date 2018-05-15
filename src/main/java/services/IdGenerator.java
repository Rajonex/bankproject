package services;

public class IdGenerator {
    private static int serviceId = 0;
    private static int bankId = 0;
    private static int clientId = 0;

    public static int generateServiceId() {
        serviceId++;
        return serviceId;
    }

    public static int generateBankId() {
        bankId++;
        return bankId;
    }

    public static int generateClientId() {
        clientId++;
        return clientId;
    }
}
